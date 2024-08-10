package com.example.coinlog.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.coinlog.R
import com.example.coinlog.auth.AuthViewModel
import com.example.coinlog.data.FinanceViewmodel
import com.example.coinlog.presentation.analytics.AnalyticsScreen
import com.example.coinlog.presentation.homeScreen.DisplayHome
import com.example.coinlog.presentation.pot.PotsScreen
import com.example.coinlog.presentation.profile.ConfirmLogout
import com.example.coinlog.presentation.profile.ProfileScreen
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Main(
    scope: CoroutineScope,
    financeViewmodel: FinanceViewmodel,
    authViewModel: AuthViewModel,
    navController: NavController,
    padding: PaddingValues
) {
    val mainScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden,
            skipHiddenState = false
        )
    )
    BottomSheetScaffold(
        scaffoldState = mainScaffoldState,
        sheetContent = {
            ConfirmLogout(
                sheetScaffoldState = mainScaffoldState,
                scope = scope,
                authViewModel = authViewModel,
                financeViewmodel = financeViewmodel
            )
        },
        sheetPeekHeight = 0.dp,
        sheetDragHandle = {}
    ) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
                .padding(padding)

        ) {
            when (financeViewmodel.selectedBottomItemIndex) {
                0 -> DisplayHome(
                    financeViewmodel = financeViewmodel,
                    navController = navController,
                    authViewModel = authViewModel
                )

                1 -> PotsScreen(financeViewmodel, navController)
                2 -> AnalyticsScreen(financeViewmodel)
                3 -> ProfileScreen(
                    authViewModel,
                    financeViewmodel,
                    navController,
                    scope = scope,
                    scaffoldState = mainScaffoldState
                )
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