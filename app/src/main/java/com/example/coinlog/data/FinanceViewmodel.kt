package com.example.coinlog.data

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class FinanceViewmodel(
    private val expenseDao: ExpenseDao,
    private val summaryDao: SummaryDao,
    private val potDao: PotDao
) : ViewModel() {

    var selectedItemIndexInEx by mutableIntStateOf(1)
    var selectedBottomItemIndex by mutableIntStateOf(0)
    var selectedCategory by mutableStateOf(Category.Miscellaneous)
    var selectedFilter by mutableStateOf(TransactionFilter.DAY)

    private val tag = "mytag"

    private val _incomeExpensesData = MutableStateFlow<Pair<List<ChartData>, List<ChartData>>>(
        Pair(
            emptyList(), emptyList()
        )
    )
    val incomeExpensesData = _incomeExpensesData.asStateFlow()

    private val _allExpenses = MutableStateFlow<List<Expenses>>(emptyList())
    val allExpenses = _allExpenses.asStateFlow()

    private val _allExpensesByCat = MutableStateFlow<List<Expenses>>(emptyList())
    val allExpensesByCat = _allExpensesByCat.asStateFlow()

    private val _allPotExpenses = MutableStateFlow<List<Expenses>>(emptyList())
    val allPotExpenses = _allPotExpenses.asStateFlow()

    private val _currentSummary = MutableStateFlow<Summary>(Summary())
    val currentSummary = _currentSummary.asStateFlow()

    private val _transactionDataFetched = MutableStateFlow<Expenses?>(null)
    val transactionDataFetched = _transactionDataFetched.asStateFlow()

    private val _allPots = MutableStateFlow<List<Pot>>(emptyList())
    val allPots = _allPots.asStateFlow()

    private val _potById = MutableStateFlow<Pot>(Pot(dateAdded = System.currentTimeMillis()))
    val potById = _potById.asStateFlow()

    init {
        loadAllExpenses()
        viewModelScope.launch {
            _currentSummary.update {
                summaryDao.getSummary() ?: Summary()
            }
        }
    }

    fun getIncomeExpenseData() {
        viewModelScope.launch {
            val incomeData = mutableListOf<ChartData>()
            val expenditureData = mutableListOf<ChartData>()

            expenseDao.getAllExpenses().collectLatest { expense ->
                expense.forEach {
                    if (it.credit) incomeData.add(ChartData(it.amount, it.dateAdded))
                    else expenditureData.add(ChartData(it.amount, it.dateAdded))
                }
                _incomeExpensesData.update {
                    Pair(incomeData, expenditureData)
                }
            }
        }
    }

    fun getExpenseFromId(id: Int) {
        viewModelScope.launch {
            _transactionDataFetched.update {
                expenseDao.getExpenseById(id)
            }
        }
    }

    private fun loadAllExpenses() {
        viewModelScope.launch {
            expenseDao.getAllExpenses().collectLatest { result ->
                _allExpenses.update {
                    result
                }
            }
        }
    }

    fun saveExpense(expenses: Expenses) {
        viewModelScope.launch {
            expenseDao.upsertExpense(expenses)
        }

        updateSummaryOnSave(expenses.amount, expenses.credit)
        loadAllExpenses()
    }

    fun updateExpense(oldExpense: Expenses, expenses: Expenses) {
        viewModelScope.launch {
            val currentSummary = summaryDao.getSummary() ?: Summary()

            var inc = currentSummary.income

            var exp = currentSummary.expenditure


            if (expenses.credit) inc -= oldExpense.amount
            else exp -= oldExpense.amount
            val newSummary = currentSummary.copy(
                income = inc, expenditure = exp, balance = inc - exp
            )

            _currentSummary.update {
                newSummary
            }
            Log.d(tag, expenses.toString())
            summaryDao.upsertSummary(newSummary)

            saveExpense(expenses)
        }
    }

    fun deleteExpense(expenses: Expenses) {
        viewModelScope.launch {
            expenseDao.deleteExpense(expenses)
        }
        updateSummaryOnDelete(expenses.amount, expenses.credit)
        loadAllExpenses()
    }

    private fun updateSummaryOnSave(amt: Double, credit: Boolean) {
        viewModelScope.launch {
            val currentSummary = summaryDao.getSummary() ?: Summary()
            var inc = currentSummary.income
            var exp = currentSummary.expenditure

            if (credit) inc += amt
            else exp += amt

            val newSummary = currentSummary.copy(
                income = inc, expenditure = exp, balance = inc - exp
            )
            _currentSummary.update {
                newSummary
            }
            summaryDao.upsertSummary(newSummary)
        }
    }

    private fun updateSummaryOnDelete(amt: Double, credit: Boolean) {
        viewModelScope.launch {
            val currentSummary = summaryDao.getSummary() ?: Summary()
            var inc = currentSummary.income
            var exp = currentSummary.expenditure

            if (credit) {
                inc -= amt
            } else {
                exp -= amt
            }

            val newSummary = currentSummary.copy(
                income = inc, expenditure = exp, balance = inc - exp
            )
            _currentSummary.update {
                newSummary
            }
            summaryDao.upsertSummary(newSummary)
        }
    }

    fun getExpensesByCategory(category: Category) {
        viewModelScope.launch {
            expenseDao.getExpensesByCategory(category).collectLatest { result ->
                _allExpensesByCat.update {
                    result
                }
            }
        }
    }

    fun loadAllPots() {
        viewModelScope.launch {
            potDao.getAlLPots().collectLatest { result ->
                _allPots.update {
                    result
                }
            }
        }
    }

    fun savePot(pot: Pot) {
        val newExpense = Expenses(
            title = pot.title,
            description = "Pot Created",
            category = Category.Pot,
            credit = false,
            amount = pot.amount,
            dateAdded = pot.dateAdded
        )
        viewModelScope.launch {
            val a: Long = potDao.upsertPot(pot)
            saveExpense(newExpense.copy(potId = a))
            loadAllPots()
        }

    }

    fun updatePot(pot: Pot) {
        viewModelScope.launch {
            potDao.upsertPot(pot)
        }
    }

    fun deletePot(pot: Pot) {
        viewModelScope.launch {
            potDao.deletePot(pot)
        }
        loadAllPots()
    }

    fun loadAllPotExpenses(potId: Long) {
        viewModelScope.launch {
            expenseDao.getPotExpensesById(potId = potId).collectLatest { result ->
                _allPotExpenses.update {
                    result
                }
            }
        }
    }

    fun getPotById(potId: Long) {
        viewModelScope.launch {
            val pot = potDao.getPotByID(potId)
            _potById.update {
                pot
            }
        }
    }

}