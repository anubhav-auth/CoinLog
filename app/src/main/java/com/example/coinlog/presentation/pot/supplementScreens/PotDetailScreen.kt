package com.example.coinlog.presentation.pot.supplementScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.coinlog.data.FinanceViewmodel
import com.example.coinlog.data.Pot
import com.example.coinlog.presentation.homeScreen.TransactionMenu
import com.example.coinlog.presentation.homeScreen.toMoneyFormat
import java.text.SimpleDateFormat

@Composable
fun PotDetailScreen(
    viewmodel: FinanceViewmodel,
    modifier: Modifier = Modifier,
    navController: NavController,
    potId: Long
) {
    viewmodel.getPotById(potId)
    viewmodel.loadAllPotExpenses(potId)
    val pot by viewmodel.potById.collectAsState()
    val potExpenses by viewmodel.allPotExpenses.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val formatter = SimpleDateFormat("EEE, dd MMM \u2022 hh:mm a")
        val datetime = formatter.format(pot.dateAdded)
        Spacer(modifier = Modifier.size(50.dp))
        Text(
            text = "CoinLog",
            fontFamily = FontFamily.Cursive,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 33.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.size(21.dp))

        Text(
            text = datetime,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.size(21.dp))
        PotDetailCard(pot = pot)
        Spacer(modifier = Modifier.height(21.dp))
        PotDetailButtons(viewmodel = viewmodel, navController = navController, id = potId)

        Spacer(modifier = Modifier.height(21.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recent Expenses",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "See all",
                color = Color.Gray,
                textDecoration = TextDecoration.Underline
            )
        }
        Spacer(modifier = Modifier.height(21.dp))
        TransactionMenu(
            items = potExpenses,
            navController = navController,
            limitNoOfElements = false,
            isMainTransaction = false
        )
    }
}

@Composable
fun PotDetailCard(pot: Pot) {

    Card(
        modifier = Modifier
            .width(width = 363.dp)
            .height(150.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ), elevation = CardDefaults.cardElevation(defaultElevation = 20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = pot.title,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Serif
                )
                Spacer(modifier = Modifier.size(21.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total Money Saved",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W500
                    )
                    Text(
                        text = "\u20b9 ${pot.amount.toMoneyFormat()}",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

            }
        }
    }
}

@Composable
fun PotDetailButtons(viewmodel: FinanceViewmodel, navController: NavController, id:Long) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = {
                navController.navigate("add_withdraw_page/${true}/${id}")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Add Money")
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 9.dp)
        ) {
            Button(
                onClick = {

                },
                modifier = Modifier
                    .padding(end = 9.dp)
                    .fillMaxWidth(0.5f)
            ) {
                Text(text = "Delete and withdraw all", overflow = TextOverflow.Clip)

            }
            Button(
                onClick = {
                    navController.navigate("add_withdraw_page/${false}/${id}")
                },
                modifier = Modifier
                    .padding(start = 9.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Withdraw")
            }
        }
    }
}