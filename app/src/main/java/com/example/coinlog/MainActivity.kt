package com.example.coinlog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.coinlog.data.FinanceDatabase
import com.example.coinlog.data.FinanceViewmodel
import com.example.coinlog.presentation.AddScreen
import com.example.coinlog.presentation.CategoriesPage
import com.example.coinlog.presentation.Main
import com.example.coinlog.presentation.TransactionDescription
import com.example.coinlog.ui.theme.CoinLogTheme


class MainActivity : ComponentActivity() {

    private val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            FinanceDatabase::class.java,
            "finance.db"
        ).build()
    }

    private val financeViewmodel by viewModels<FinanceViewmodel>(factoryProducer = {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return FinanceViewmodel(database.expensesDao(), database.summaryDao()) as T
            }
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoinLogTheme {
                val data by financeViewmodel.currentSummary.collectAsState()
                val navController = rememberNavController()
                Scaffold { innerPadding ->
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .fillMaxSize()
                    ) {
                        NavHost(navController = navController, startDestination = "home_screen") {
                            composable("home_screen") {
                                Main(
                                    viewmodel = financeViewmodel,
                                    navController = navController
                                )
                            }
                            composable("add_screen") {
                                AddScreen(
                                    paddingValues = innerPadding,
                                    viewmodel = financeViewmodel,
                                    navController = navController
                                )
                            }
                            composable("categories_page") {
                                CategoriesPage()
                            }
                            composable("transaction_description/{id}") {
                                val id = it.arguments?.getString("id")?.toInt() ?: 3
                                TransactionDescription(id = id, viewmodel = financeViewmodel, navController = navController)
                            }
                        }
                    }
                }
            }
        }
    }
}
