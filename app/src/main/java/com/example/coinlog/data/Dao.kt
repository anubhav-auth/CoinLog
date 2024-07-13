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
}

@Dao
interface SummaryDao {

    @Upsert
    suspend fun upsertExpense(summary: Summary)

    @Query("SELECT * FROM SUMMARY WHERE id = 1")
    suspend fun getSummary(): Summary?
}