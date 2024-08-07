package com.example.coinlog.presentation.homeScreen

import android.annotation.SuppressLint
import android.icu.text.DecimalFormat
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.coinlog.R
import com.example.coinlog.auth.AuthViewModel
import com.example.coinlog.data.Category
import com.example.coinlog.data.Expenses
import com.example.coinlog.data.FinanceViewmodel
import com.example.coinlog.data.HelperObj
import com.example.coinlog.data.TransactionFilter
import com.example.coinlog.presentation.profile.firstName
import java.text.DateFormat.getDateInstance
import java.text.SimpleDateFormat
import java.util.Date

fun Double.toMoneyFormat(): String {
    val formatter = DecimalFormat("#,##,###.##")
    return formatter.format(this)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DisplayHome(
    financeViewmodel: FinanceViewmodel,
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val imageHeader = authViewModel.auth.currentUser?.photoUrl
    val name = authViewModel.auth.currentUser?.displayName

    val allExpenses by financeViewmodel.allExpenses.collectAsState()
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

        }
    ) { _ ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 12.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                AsyncImage(
                    model = imageHeader,
                    contentDescription = "",
                    modifier = Modifier
                        .clip(
                            CircleShape
                        )
                        .size(42.dp)
                )
                Spacer(modifier = Modifier.width(21.dp))
                Text(
                    text = "Hi ${name?.firstName()}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
//                AsyncImage(
//                    model = imageHeader,
//                    contentDescription = "",
//                    modifier = Modifier
//                        .clip(
//                            CircleShape
//                        )
//                        .size(42.dp)
//                )
            }
            Spacer(modifier = Modifier.height(21.dp))
            BalanceCard(viewmodel = financeViewmodel)
            Spacer(modifier = Modifier.height(21.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Categories",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "See all",
                    color = Color.Gray,
                    modifier = Modifier.clickable { navController.navigate("categories_page") },
                    textDecoration = TextDecoration.Underline
                )
            }
            Spacer(modifier = Modifier.height(21.dp))
            CategoriesMenu(
                items = listOf(
                    CategoriesContent(Category.Miscellaneous),
                    CategoriesContent(Category.FoodAndDrinks),
                    CategoriesContent(Category.PersonalCare),
                    CategoriesContent(Category.BillsAndUtilities),
                    CategoriesContent(Category.Commute),
                    CategoriesContent(Category.Travel)
                ),
                navController = navController
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
                    modifier = Modifier.clickable { navController.navigate("all_expenses_screen/${false}") },
                    textDecoration = TextDecoration.Underline
                )
            }
            Spacer(modifier = Modifier.height(21.dp))
            TransactionMenu(
                items = allExpenses,
                navController = navController,
                limitNoOfElements = true
            )
        }
    }
}

@Composable
fun BalanceCard(viewmodel: FinanceViewmodel) {

    val summary by viewmodel.currentSummary.collectAsState()
    Box(
        Modifier
            .clip(RoundedCornerShape(21.dp))
            .shadow(15.dp)
            .size(width = 363.dp, height = 204.dp)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total Balance",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.size(3.dp))
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "",
                        modifier = Modifier.size(15.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Text(
                    text = "\u20b9 ${summary.balance.toMoneyFormat()}",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }


            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(21.dp))
                    .fillMaxWidth()
                    .fillMaxHeight(0.75f)
                    .background(Color.Black),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.finance_expense),
                        contentDescription = "expense",
                        tint = Color.White,
                        modifier = Modifier.size(21.dp)
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    Column {
                        Text(
                            text = "Expense so far",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Light
                        )
                        Text(
                            text = "₹${summary.expenditure.toMoneyFormat()}",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                VerticalDivider(
                    thickness = 3.dp,
                    color = Color.White,
                    modifier = Modifier.padding(vertical = 21.dp)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.finance_income),
                        contentDescription = "expense",
                        tint = Color.White,
                        modifier = Modifier.size(21.dp)
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    Column {
                        Text(
                            text = "Income so far",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Light
                        )
                        Text(
                            text = "₹${summary.income.toMoneyFormat()}",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }

}

@Composable
fun CategoriesMenu(items: List<CategoriesContent>, navController: NavController) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(items) { item ->
            CategoriesItem(item = item, navController = navController)
        }
    }
}

@Composable
fun CategoriesItem(item: CategoriesContent, navController: NavController) {

    Column(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .clickable { navController.navigate("filter_by_category_page/${item.category.ordinal}") },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = HelperObj.getIcon(item.category)),
                contentDescription = item.category.name,
                tint = Color.Black,
                modifier = Modifier
                    .size(48.dp)
                    .padding(6.dp)
            )
        }
        Text(
            text = item.category.toString(),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

data class CategoriesContent(val category: Category)

fun groupExpensesByDate(expenses: List<Expenses>): Map<String, List<Expenses>> {
    val dateFormat = SimpleDateFormat("EEE, dd MMM")
    return expenses.groupBy { expense ->
        dateFormat.format(Date(expense.dateAdded))
    }
}

fun groupExpensesByMonth(expenses: List<Expenses>): Map<String, List<Expenses>> {
    val dateFormat = SimpleDateFormat("MMM, yyyy")
    return expenses.groupBy { expense ->
        dateFormat.format(Date(expense.dateAdded))
    }
}
fun groupExpensesByYear(expenses: List<Expenses>): Map<String, List<Expenses>> {
    val dateFormat = SimpleDateFormat("yyyy")
    return expenses.groupBy { expense ->
        dateFormat.format(Date(expense.dateAdded))
    }
}


@Composable
fun TransactionMenu(
    items: List<Expenses>,
    navController: NavController,
    limitNoOfElements: Boolean = false,
    isMainTransaction: Boolean = true,
    groupBy: TransactionFilter = TransactionFilter.DAY,
    insidePotScreen: Boolean = false
) {

    val groupedExpenses: Map<String, List<Expenses>> = if (groupBy == TransactionFilter.DAY) {
        if (limitNoOfElements) {
            groupExpensesByDate(items.take(15))
        } else {
            groupExpensesByDate(items)
        }
    } else if (groupBy == TransactionFilter.MONTH) {
        groupExpensesByMonth(items)
    } else {
        groupExpensesByYear(items)
    }

    LazyColumn(contentPadding = PaddingValues(bottom = 135.dp)) {
        groupedExpenses.forEach { (date, expensesOnDate) ->
            item {
                Text(
                    text = date,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.W400,
                    modifier = Modifier.padding(12.dp)
                )
            }
            items(expensesOnDate) { item ->
                TransactionItem(
                    item = item,
                    navController = navController,
                    isMainTransaction,
                    insidePotScreen
                )
            }
        }


    }
}

@Composable
fun TransactionItem(
    item: Expenses,
    navController: NavController,
    isMainTransaction: Boolean,
    insidePotScreen: Boolean
) {
    Row(
        modifier = Modifier
            .padding(bottom = 6.dp)
            .clip(RoundedCornerShape(15.dp))
            .clickable {
                navController.navigate("transaction_description/${item.id}")
            }
            .border(width = 1.dp, shape = RoundedCornerShape(15.dp), color = Color.Gray)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxWidth()
            .padding(6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Icon(
                    painter = painterResource(
                        id = HelperObj.getIcon(
                            if (!isMainTransaction && insidePotScreen) {
                                item.category
                            } else if (isMainTransaction && item.potId == null) {
                                item.category
                            } else if (isMainTransaction && !insidePotScreen) {
                                Category.Pot
                            } else {
                                Category.Warning
                            }
                        )
                    ),
                    modifier = Modifier
                        .size(39.dp)
                        .padding(6.dp),
                    contentDescription = item.category.name,
                    tint = Color.Black
                )
            }
            Spacer(modifier = Modifier.size(9.dp))
            Column(modifier = Modifier.fillMaxSize(0.55f)) {
                Text(
                    text = item.title,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = item.description,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Light,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        val dateFormat = getDateInstance()//SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val timeFormat = SimpleDateFormat("hh:mm a")
        val date = Date(item.dateAdded)

        var bool = item.credit

        if (!isMainTransaction) bool = !bool

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = if (bool) "+ ${item.amount.toMoneyFormat()}" else "- ${item.amount.toMoneyFormat()}",
                color = if (bool) Color(0xFF228C22) else Color.Red
            )
            Text(
                text = timeFormat.format(date),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}