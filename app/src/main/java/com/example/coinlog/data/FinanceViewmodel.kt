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
    private val expenseDao: ExpenseDao, private val summaryDao: SummaryDao
) : ViewModel() {

    var selectedItemIndNexInEx by mutableIntStateOf(1)
    var selectedBottomItemIndex by mutableIntStateOf(0)
    var selectedCategory by mutableStateOf(Category.Miscellaneous)

    private val tag = "mytag"
    private val _allExpenses = MutableStateFlow<List<Expenses>>(emptyList())
    val allExpenses = _allExpenses.asStateFlow()

    private val _allExpensesByCat = MutableStateFlow<List<Expenses>>(emptyList())
    val allExpensesByCat = _allExpensesByCat.asStateFlow()

    private val _currentSummary = MutableStateFlow<Summary>(Summary())
    val currentSummary = _currentSummary.asStateFlow()

    private val _transactionDataFetched = MutableStateFlow<Expenses?>(null)
    val transactionDataFetched = _transactionDataFetched.asStateFlow()

    init {
        loadAllExpenses()
        viewModelScope.launch {
            _currentSummary.update {
                summaryDao.getSummary() ?: Summary()
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
        updateSummaryOnDelete(expenses)
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

    private fun updateSummaryOnDelete(expenses: Expenses) {
        viewModelScope.launch {
            val currentSummary = summaryDao.getSummary() ?: Summary()
            var inc = currentSummary.income
            var exp = currentSummary.expenditure

            if (expenses.credit) {
                inc -= expenses.amount
            } else {
                exp -= expenses.amount
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

    fun getExpensesByCategory(category: Category){
        viewModelScope.launch {
            expenseDao.getExpensesByCategory(category).collectLatest {result->
                _allExpensesByCat.update {
                    result
                }
            }
        }
    }
}