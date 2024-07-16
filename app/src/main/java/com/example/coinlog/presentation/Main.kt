package com.example.coinlog.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.coinlog.R
import com.example.coinlog.data.FinanceViewmodel

@Composable
fun Main(
    viewmodel: FinanceViewmodel, navController: NavController
) {

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("add_screen")
                },
                modifier = Modifier.padding(bottom = 75.dp),
                containerColor = Color(0xFFD6FF65)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }

        },


        ) { paddingValue ->
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
                .padding(paddingValue)

        ) {
            Display(viewmodel = viewmodel, navController = navController)
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