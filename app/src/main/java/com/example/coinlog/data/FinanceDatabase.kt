package com.example.coinlog.data

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Expenses::class, Summary::class], version = 1)
abstract class FinanceDatabase : RoomDatabase() {
    abstract fun expensesDao(): ExpenseDao
    abstract fun summaryDao(): SummaryDao
}