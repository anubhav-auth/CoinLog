package com.example.coinlog.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.coinlog.R
import com.example.coinlog.auth.AuthViewModel
import com.example.coinlog.data.FinanceViewmodel
import com.example.coinlog.presentation.analytics.AnalyticsScreen
import com.example.coinlog.presentation.homeScreen.DisplayHome
import com.example.coinlog.presentation.pot.PotsScreen
import com.example.coinlog.presentation.profile.ProfileScreen

@Composable
fun Main(
    financeViewmodel: FinanceViewmodel,authViewModel: AuthViewModel, navController: NavController
) {

    Scaffold { paddingValue ->
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
                .padding(paddingValue)

        ) {
            when (financeViewmodel.selectedBottomItemIndex) {
                0 -> DisplayHome(financeViewmodel = financeViewmodel, navController = navController, authViewModel = authViewModel)
                1 -> PotsScreen(financeViewmodel, navController)
                2 -> AnalyticsScreen(financeViewmodel)
                3 -> ProfileScreen(authViewModel, navController)
            }

            BottomMenu(
                items = listOf(
                    BottomMenuContent("Home", R.drawable.finance_home),
                    BottomMenuContent("Pots", R.drawable.finance_pots),
                    BottomMenuContent("Analytics", R.drawable.finance_anaytics),
                    BottomMenuContent("Profile", R.drawable.finance_profile),
                ), viewmodel = financeViewmodel, modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}