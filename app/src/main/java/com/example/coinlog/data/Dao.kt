package com.example.coinlog.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Upsert
    suspend fun upsertExpense(expenses: Expenses)

    @Delete
    suspend fun deleteExpense(expenses: Expenses)

    @Query("SELECT * FROM EXPENSES ORDER BY dateAdded DESC")
    fun getAllExpenses(): Flow<List<Expenses>>

    @Query("SELECT * FROM EXPENSES WHERE id = :expenseId")
    suspend fun getExpenseById(expenseId: Int): Expenses

    @Query("SELECT * FROM EXPENSES WHERE category = :category ORDER BY dateAdded DESC")
    fun getExpensesByCategory(category: Category):Flow<List<Expenses>>
}

@Dao
interface SummaryDao {

    @Upsert
    suspend fun upsertSummary(summary: Summary)

    @Query("SELECT * FROM SUMMARY WHERE id = 1")
    suspend fun getSummary(): Summary?

    @Query("SELECT * FROM SUMMARY WHERE id = :id")
    suspend fun getSummaryById(id: Int): Summary?
}