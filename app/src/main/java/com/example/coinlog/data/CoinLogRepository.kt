package com.example.coinlog.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await

class CoinLogRepository(
    private val expenseDao: ExpenseDao,
    private val potDao: PotDao,
    private val summaryDao: SummaryDao,
    private val firestore: FirebaseFirestore
) {
    suspend fun checkAndLoadUserData(userid: String) {
        val expensesSnapShot = firestore.collection("users")
            .document(userid)
            .collection("expenses")
            .get()
            .await()

        if (!expensesSnapShot.isEmpty) {
            val expensesList = expensesSnapShot.toObjects(Expenses::class.java)
            for (expense in expensesList) {
                expenseDao.upsertExpense(expense)
            }

            // Similarly, check and load data for Summary and Pots
            // Load Summary
            val summarySnapshot = firestore.collection("users")
                .document(userid)
                .collection("summaries")
                .get()
                .addOnSuccessListener { document ->
                    Log.e("Firestore", "doneeee")
                }
                .addOnFailureListener { exception ->
                    // Handle permission denial or other errors
                    Log.e("Firestore", "Error getting document", exception)
                }
                .await()
            if (!summarySnapshot.isEmpty) {
                val summary = summarySnapshot.toObjects(Summary::class.java).first()
                summaryDao.upsertSummary(summary)
            }

            // Load Pots
            val potsSnapshot = firestore.collection("users")
                .document(userid)
                .collection("pots")
                .get()
                .await()
            if (!potsSnapshot.isEmpty) {
                val potsList = potsSnapshot.toObjects(Pot::class.java)
                for (pot in potsList) {
                    potDao.upsertPot(pot)
                }
            }
        }
    }

    suspend fun uploadUserData(userId: String) {
        // Upload expenses
        val expenses = expenseDao.getAllExpenses().first()
        for (expense in expenses) {
            firestore.collection("users")
                .document(userId)
                .collection("expenses")
                .document(expense.id.toString())
                .set(expense)
                .addOnSuccessListener { document ->
                    // Handle successful document retrieval
                    Log.e("Firestore", "done")
                }
                .addOnFailureListener { exception ->
                    // Handle permission denial or other errors
                    Log.e("Firestore", "Error getting document", exception)
                }
                .await()
        }

        // Upload summary
        val summary = summaryDao.getSummary()
        summary?.let {
            firestore.collection("users")
                .document(userId)
                .collection("summaries")
                .document(it.id.toString())
                .set(it)
                .await()
        }

        // Upload pots
        val pots = potDao.getAlLPots().first()
        for (pot in pots) {
            firestore.collection("users")
                .document(userId)
                .collection("pots")
                .document(pot.id.toString())
                .set(pot)
                .await()
        }
    }
}