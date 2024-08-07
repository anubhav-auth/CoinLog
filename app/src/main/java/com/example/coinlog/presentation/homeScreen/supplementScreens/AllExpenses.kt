package com.example.coinlog.presentation.homeScreen.supplementScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.coinlog.data.FinanceViewmodel
import com.example.coinlog.data.TransactionFilter
import com.example.coinlog.presentation.homeScreen.TransactionMenu

@Composable
fun AllExpenses(viewmodel: FinanceViewmodel, navController: NavController, isPot: Boolean) {
    val expenses by if (!isPot) viewmodel.allExpenses.collectAsState() else viewmodel.allPotExpenses.collectAsState()


    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 12.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "All Expenses",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.height(24.dp))
            FilterButton(viewmodel = viewmodel)
            Spacer(modifier = Modifier.height(24.dp))
            TransactionMenu(
                items = expenses,
                navController = navController,
                groupBy = viewmodel.selectedFilter,
                isMainTransaction = !isPot,
                insidePotScreen = isPot
            )

        }
    }
}

@Composable
fun FilterButton(viewmodel: FinanceViewmodel) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceAround) {
        Button(onClick = { viewmodel.selectedFilter = TransactionFilter.DAY }) {
            Text(
                text = "Day",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Button(onClick = { viewmodel.selectedFilter = TransactionFilter.MONTH }) {
            Text(
                text = "Month",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Button(onClick = { viewmodel.selectedFilter = TransactionFilter.YEAR }) {
            Text(
                text = "Year",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}