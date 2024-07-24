package com.example.coinlog.presentation.pot.supplementScreens

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.coinlog.data.Category
import com.example.coinlog.data.Expenses
import com.example.coinlog.data.FinanceViewmodel
import com.example.coinlog.data.Pot
import com.example.coinlog.presentation.homeScreen.TransactionMenu
import com.example.coinlog.presentation.homeScreen.toMoneyFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PotDetailScreen(
    viewmodel: FinanceViewmodel,
    modifier: Modifier = Modifier,
    navController: NavController,
    potId: Long,
    scope: CoroutineScope
) {

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden,
            skipHiddenState = false
        )
    )
    viewmodel.getPotById(potId)
    viewmodel.loadAllPotExpenses(potId)
    val pot by viewmodel.potById.collectAsState()
    val potExpenses by viewmodel.allPotExpenses.collectAsState()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "The balance will be transferred to the main balance and the pot will be deleted",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = {
                        scope.launch {
                            scaffoldState.bottomSheetState.hide()
                        }
                    }) {
                        Text(
                            text = "Cancel",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = {
                            val newExpense = Expenses(
                                title = "Pot",
                                description = "${pot.title} destroyed",
                                category = Category.Pot,
                                credit = true,
                                amount = pot.amount,
                                dateAdded = System.currentTimeMillis(),
                                potId = pot.id
                            )
                            scope.launch {
                                scaffoldState.bottomSheetState.hide()
                            }
                            navController.navigateUp()
                            viewmodel.saveExpense(newExpense)
                            viewmodel.deletePot(pot)
                        }) {
                        Text(
                            text = "Confirm",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        },
        sheetPeekHeight = 0.dp
    ) {
        pot.let { pot ->
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
                PotDetailButtons(
                    scope,
                    navController = navController,
                    id = potId,
                    scaffoldState = scaffoldState
                )

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
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable { navController.navigate("all_expenses_screen/${true}") }
                    )
                }
                Spacer(modifier = Modifier.height(21.dp))
                TransactionMenu(
                    items = potExpenses,
                    navController = navController,
                    isMainTransaction = false,
                    insidePotScreen = true
                )
            }
        }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PotDetailButtons(
    scope: CoroutineScope,
    navController: NavController,
    id: Long,
    scaffoldState: BottomSheetScaffoldState
) {
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
                    scope.launch {
                        scaffoldState.bottomSheetState.expand()
                    }
                },
                modifier = Modifier
                    .padding(end = 9.dp)
                    .fillMaxWidth(0.5f)
            ) {
                Text(text = "Destroy Pot", overflow = TextOverflow.Clip)

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