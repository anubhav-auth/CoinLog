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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.coinlog.auth.AuthState
import com.example.coinlog.auth.AuthViewModel
import com.example.coinlog.auth.LoginPage
import com.example.coinlog.auth.SignUpPage
import com.example.coinlog.data.FinanceDatabase
import com.example.coinlog.data.FinanceViewmodel
import com.example.coinlog.presentation.Main
import com.example.coinlog.presentation.homeScreen.supplementScreens.AddScreen
import com.example.coinlog.presentation.homeScreen.supplementScreens.AllExpenses
import com.example.coinlog.presentation.homeScreen.supplementScreens.CategoriesPage
import com.example.coinlog.presentation.homeScreen.supplementScreens.EditData
import com.example.coinlog.presentation.homeScreen.supplementScreens.FilterByCategoryPage
import com.example.coinlog.presentation.homeScreen.supplementScreens.TransactionDescription
import com.example.coinlog.presentation.pot.supplementScreens.AddWithdrawMoneyToPot
import com.example.coinlog.presentation.pot.supplementScreens.PotAddScreen
import com.example.coinlog.presentation.pot.supplementScreens.PotDetailScreen
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
                return FinanceViewmodel(
                    database.expensesDao(),
                    database.summaryDao(),
                    database.potDao()
                ) as T
            }
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val authViewModel: AuthViewModel by viewModels()



        setContent {
            CoinLogTheme {
                val authState by authViewModel.authState.collectAsState()

                val scope = rememberCoroutineScope()
                val navController = rememberNavController()
                Scaffold { innerPadding ->
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .fillMaxSize()
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = when (authState) {
                                AuthState.Authenticated -> "home_screen"
                                else -> "login_page"
                            }
                        ) {
                            composable("login_page") {
                                LoginPage(viewModel = authViewModel,
                                    navController = navController,
                                    scope = scope)
                            }
                            composable("signup_page") {
                                SignUpPage(viewModel = authViewModel, navController = navController)
                            }
                            composable("home_screen") {
                                Main(
                                    financeViewmodel = financeViewmodel,
                                    authViewModel = authViewModel,
                                    navController = navController
                                )
                            }
                            composable("add_screen") {
                                AddScreen(
                                    paddingValues = innerPadding,
                                    viewmodel = financeViewmodel,
                                    navController = navController,
                                    scope = scope
                                )
                            }
                            composable("categories_page") {
                                CategoriesPage(navController = navController)
                            }
                            composable("all_expenses_screen/{isPot}") {
                                val isPot = it.arguments?.getString("isPot").toBoolean()
                                AllExpenses(
                                    viewmodel = financeViewmodel,
                                    navController = navController,
                                    isPot
                                )
                            }
                            composable("transaction_description/{id}") {
                                val id = it.arguments?.getString("id")?.toInt() ?: 3
                                TransactionDescription(
                                    id = id,
                                    viewmodel = financeViewmodel,
                                    navController = navController,
                                    scope = scope
                                )
                            }
                            composable("edit_transaction") {
                                EditData(
                                    navController = navController,
                                    viewmodel = financeViewmodel
                                )
                            }
                            composable("filter_by_category_page/{index}") {
                                val index = it.arguments?.getString("index")?.toInt() ?: 0
                                FilterByCategoryPage(
                                    id = index,
                                    viewmodel = financeViewmodel,
                                    navController = navController
                                )
                            }
                            composable("pot_details/{id}") {
                                val id = it.arguments?.getString("id")?.toLong() ?: 0
                                PotDetailScreen(
                                    viewmodel = financeViewmodel,
                                    navController = navController,
                                    potId = id,
                                    scope = scope
                                )
                            }
                            composable("new_pot_add") {
                                PotAddScreen(
                                    viewmodel = financeViewmodel,
                                    navController = navController
                                )
                            }
                            composable("add_withdraw_page/{add}/{id}") {
                                val add = it.arguments?.getString("add").toBoolean()
                                val id = it.arguments?.getString("id")?.toLong() ?: 0
                                AddWithdrawMoneyToPot(
                                    paddingValues = innerPadding,
                                    viewmodel = financeViewmodel,
                                    navController = navController,
                                    add = add,
                                    potId = id,
                                    scope = scope
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
