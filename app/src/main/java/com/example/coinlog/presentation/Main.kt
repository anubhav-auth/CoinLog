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
import com.example.coinlog.data.FinanceViewmodel
import com.example.coinlog.presentation.analytics.AnalyticsScreen
import com.example.coinlog.presentation.homeScreen.DisplayHome
import com.example.coinlog.presentation.pot.PotsScreen
import com.example.coinlog.profile.ProfileScreen

@Composable
fun Main(
    viewmodel: FinanceViewmodel, navController: NavController
) {

    Scaffold{ paddingValue ->
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
                .padding(paddingValue)

        ) {
            when (viewmodel.selectedBottomItemIndex) {
                0 -> DisplayHome(viewmodel = viewmodel, navController = navController)
                1 -> PotsScreen(viewmodel, navController)
                2 -> AnalyticsScreen()
                3 -> ProfileScreen()
            }

            BottomMenu(
                items = listOf(
                    BottomMenuContent("Home", R.drawable.finance_home),
                    BottomMenuContent("Pots", R.drawable.finance_pots),
                    BottomMenuContent("Analytics", R.drawable.finance_anaytics),
                    BottomMenuContent("Profile", R.drawable.finance_profile),
                ), viewmodel = viewmodel, modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}