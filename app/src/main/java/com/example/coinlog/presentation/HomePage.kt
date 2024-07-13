package com.example.coinlog.presentation

import android.icu.text.DecimalFormat
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.coinlog.R
import com.example.coinlog.data.Category
import com.example.coinlog.data.Expenses
import com.example.coinlog.data.FinanceViewmodel
import com.example.coinlog.data.HelperObj
import com.example.coinlog.ui.theme.financeCardYellow
import java.text.DateFormat.getDateInstance
import java.text.DateFormat.getTimeInstance
import java.util.Date

fun Double.toMoneyFormat(): String {
    val formatter = DecimalFormat("#,##,###.##")
    return formatter.format(this)
}

@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    viewmodel: FinanceViewmodel,
    navController: NavController
) {

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("add_screen")
                },
                modifier = Modifier.padding(bottom = 75.dp)
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "")
            }
        }

    ) { paddingValue ->
        val a = paddingValue
        Box(
            modifier = Modifier
                .background(Color(0xFF1D1D1D))
                .fillMaxSize()
                .padding(paddingValue)

        ) {
            Display(viewmodel = viewmodel)
            BottomMenu(
                items = listOf(
                    BottomMenuContent("home", R.drawable.ic_launcher_background),
                    BottomMenuContent("home", R.drawable.ic_launcher_background),
                    BottomMenuContent("home", R.drawable.ic_launcher_background),
                    BottomMenuContent("home", R.drawable.ic_launcher_background),
                ),
                viewmodel = viewmodel,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
fun Display(viewmodel: FinanceViewmodel, modifier: Modifier = Modifier) {
    val imageHeader = R.drawable.ic_launcher_background
    val name = "default"
    val allExpenses by viewmodel.allExpenses.collectAsState()
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
            Image(
                painter = painterResource(id = imageHeader),
                contentDescription = "",
                modifier = Modifier
                    .clip(
                        CircleShape
                    )
                    .size(42.dp)
            )
            Spacer(modifier = Modifier.width(21.dp))
            Text(text = "Hi $name", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        }
        Spacer(modifier = Modifier.height(21.dp))
        BalanceCard(viewmodel = viewmodel)
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
                color = Color.White
            )
            Text(text = "See all", color = Color.LightGray)
        }
        Spacer(modifier = Modifier.height(21.dp))
        CategoriesMenu(
            items = listOf(
                CategoriesContent(R.drawable.ic_launcher_background, Category.Miscellaneous),
                CategoriesContent(R.drawable.ic_launcher_background, Category.Miscellaneous),
                CategoriesContent(R.drawable.ic_launcher_background, Category.Miscellaneous),
                CategoriesContent(R.drawable.ic_launcher_background, Category.Miscellaneous),
                CategoriesContent(R.drawable.ic_launcher_background, Category.Miscellaneous),
            )
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
                color = Color.White
            )
            Text(text = "See all", color = Color.LightGray)
        }
        Spacer(modifier = Modifier.height(21.dp))
        TransactionMenu(items = allExpenses)
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
            .background(financeCardYellow)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
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
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.size(3.dp))
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "",
                        modifier = Modifier.size(15.dp),
                        tint = Color.Black
                    )
                }
                Text(
                    text = "\u20b9 ${summary.balance.toMoneyFormat()}",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold, color = Color.Black
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
fun CategoriesMenu(items: List<CategoriesContent>) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach {
            CategoriesItem(item = it)
        }
    }
}

@Composable
fun CategoriesItem(item: CategoriesContent) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .size(51.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = HelperObj.getIcon(item.category)),
            contentDescription = item.category.name,
            tint = Color.White
        )
    }
}

data class CategoriesContent(
    val iconId: Int,
    val category: Category
)

@Composable
fun TransactionMenu(items: List<Expenses>) {
    LazyColumn(contentPadding = PaddingValues(bottom = 135.dp)) {
        items(items) { item ->
            TransactionItem(item = item)
        }
    }
}

@Composable
fun TransactionItem(item: Expenses) {
    Row(
        modifier = Modifier
            .padding(bottom = 6.dp)
            .clip(RoundedCornerShape(15.dp))
            .border(width = 1.dp, shape = RoundedCornerShape(15.dp), color = Color.Gray)
            .fillMaxWidth()
            .padding(6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = HelperObj.getIcon(item.category)),
                modifier = Modifier
                    .clip(CircleShape)
                    .size(42.dp),
                contentDescription = item.category.name,
                tint = Color.White
            )
            Spacer(modifier = Modifier.size(9.dp))
            Column {
                Text(text = item.title, color = Color.White)
                Text(
                    text = item.description.split(" ").take(5).joinToString(" "),
                    color = Color.White
                )
            }
        }
        val dateFormat = getDateInstance()//SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val timeFormat = getTimeInstance()//SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = Date(item.dateAdded)

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = if (item.credit) "+ ${item.amount.toMoneyFormat()}" else "- ${item.amount.toMoneyFormat()}",
                color = if (item.credit) Color.Green else Color.Red
            )
            Text(text = timeFormat.format(date), color = Color.White)
        }
    }
}