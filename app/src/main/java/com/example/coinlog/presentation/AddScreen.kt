package com.example.coinlog.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.coinlog.data.Category
import com.example.coinlog.data.Expenses
import com.example.coinlog.data.FinanceViewmodel
import com.example.coinlog.ui.theme.financeAddSelected
import com.example.coinlog.ui.theme.financePurple

@Composable
fun AddScreen(
    paddingValues: PaddingValues,
    viewmodel: FinanceViewmodel,
    navController: NavController
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        TopChoice(viewmodel = viewmodel)
        CategoryChoice()
        dataPlace(navController = navController, viewmodel = viewmodel)
    }


}

@Composable
fun TopChoice(viewmodel: FinanceViewmodel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .clickable { viewmodel.selectedItemIndexInEx = 0 },
            contentAlignment = Alignment.Center

        ) {
            Text(
                text = if (0 == viewmodel.selectedItemIndexInEx) "\u2022 Income" else "Income",
                color = if (0 == viewmodel.selectedItemIndexInEx) financeAddSelected else Color.LightGray,
                fontWeight = if (0 == viewmodel.selectedItemIndexInEx) FontWeight.Bold else FontWeight.Normal,
                fontSize = if (0 == viewmodel.selectedItemIndexInEx) 24.sp else 21.sp,
            )
        }
        VerticalDivider(
            modifier = Modifier.padding(6.dp),
            thickness = 2.dp,
            color = Color.LightGray
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .clickable { viewmodel.selectedItemIndexInEx = 1 },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (1 == viewmodel.selectedItemIndexInEx) "\u2022 Expense" else "Expense",
                modifier = Modifier.clickable { viewmodel.selectedItemIndexInEx = 1 },
                color = if (1 == viewmodel.selectedItemIndexInEx) financeAddSelected else Color.LightGray,
                fontWeight = if (1 == viewmodel.selectedItemIndexInEx) FontWeight.Bold else FontWeight.Normal,
                fontSize = if (1 == viewmodel.selectedItemIndexInEx) 21.sp else 15.sp
            )
        }
    }
}

@Composable
fun CategoryChoice(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(21.dp))
            .fillMaxWidth()
            .height(50.dp)
            .background(financePurple)
    ) {

    }
}

@Composable
fun dataPlace(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewmodel: FinanceViewmodel
) {
    var titleText by remember {
        mutableStateOf("")
    }
    var descriptionText by remember {
        mutableStateOf("")
    }
    var amountText by remember {
        mutableStateOf("")
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TextField(value = titleText, onValueChange = {
            titleText = it
        })
        TextField(value = descriptionText, onValueChange = {
            descriptionText = it
        })
        TextField(value = amountText, onValueChange = {
            amountText = it
        })

        val newExpense = Expenses(
            title = titleText,
            description = descriptionText,
            category = Category.Miscellaneous,
            credit = viewmodel.selectedItemIndexInEx == 0,
            amount = if (amountText.isNotBlank()) amountText.toDouble() else 0.0,
            dateAdded = System.currentTimeMillis()
        )

        Row {
            Button(onClick = {
                titleText = ""
                descriptionText = ""
                amountText = ""
                navController.navigateUp()
            }) {
                Text(text = "cancel")
            }
            Button(onClick = {
                titleText = ""
                descriptionText = ""
                amountText = ""
                viewmodel.saveExpense(newExpense)
                navController.navigateUp()
            }) {
                Text(text = "save")
            }
        }

    }
}