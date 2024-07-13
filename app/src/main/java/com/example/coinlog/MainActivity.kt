package com.example.coinlog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.coinlog.data.FinanceDatabase
import com.example.coinlog.data.FinanceViewmodel
import com.example.coinlog.presentation.AddScreen
import com.example.coinlog.presentation.HomePage


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
            val data by financeViewmodel.currentSummary.collectAsState()
            val navController = rememberNavController()
            Scaffold { innerPadding ->
                Box(
                    modifier = Modifier
                        .background(Color(0xFF1D1D1D))
                        .fillMaxSize()
                ) {
                    NavHost(navController = navController, startDestination = "home_screen") {
                        composable("home_screen") {
                            HomePage(
                                modifier = Modifier.padding(innerPadding),
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
                    }
                }
            }
        }
    }
}
