package com.example.coinlog.presentation.homeScreen.supplementScreens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.coinlog.R
import com.example.coinlog.data.Category
import com.example.coinlog.data.FinanceViewmodel
import com.example.coinlog.data.HelperObj
import com.example.coinlog.presentation.homeScreen.CategoriesContent
import com.example.coinlog.presentation.homeScreen.toMoneyFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDescription(
    id: Int,
    viewmodel: FinanceViewmodel,
    navController: NavController,
    scope: CoroutineScope
) {
    val context = LocalContext.current
    viewmodel.getExpenseFromId(id)
    val expense by viewmodel.transactionDataFetched.collectAsState()
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden, skipHiddenState = false
        )
    )
    expense?.let {

        viewmodel.selectedCategory = it.category

        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetContent = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Your spend was Categorised",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(text = "Tap to change it", fontSize = 9.sp, fontWeight = FontWeight.Normal)

                    Spacer(modifier = Modifier.size(30.dp))
                    LazyVerticalGrid(columns = GridCells.Fixed(3)) {
                        val categories = Category.entries.toTypedArray()
                        val categoriesToSkip = listOf(Category.Pot, Category.Warning)
                        val filteredCategory = categories.filter { it !in categoriesToSkip }
                        items(filteredCategory) {
                            CategoriesSheetItem(item = CategoriesContent(it), viewmodel = viewmodel)
                        }
                    }
                    Spacer(modifier = Modifier.size(21.dp))
                    Button(
                        onClick = {
                            scope.launch {
                                scaffoldState.bottomSheetState.hide()
                            }
                        },
                        modifier = Modifier
                            .padding(12.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .fillMaxWidth()
                    ) {
                        Text(text = "Apply")
                    }
                }
            },
            sheetPeekHeight = 0.dp,
            sheetShadowElevation = 21.dp
        ) { paddingVal ->
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues = paddingVal)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    val timeFormat = SimpleDateFormat("hh:mm a")
                    val dateFormatter = SimpleDateFormat("EEE, dd MMM")
                    val time = timeFormat.format(it.dateAdded)
                    val date = dateFormatter.format(it.dateAdded)
                    Spacer(modifier = Modifier.size(50.dp))
                    Text(
                        text = "CoinLog",
                        fontFamily = FontFamily.Cursive,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 33.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.size(20.dp))

                    Text(
                        text = "$date \u2022 $time",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.size(20.dp))
                    Card(
                        modifier = Modifier
                            .width(width = 363.dp)
                            .heightIn(min = 204.dp, max = 350.dp), colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ), elevation = CardDefaults.cardElevation(defaultElevation = 20.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 204.dp, max = 350.dp),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(MaterialTheme.colorScheme.secondaryContainer),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            painter = painterResource(
                                                id = HelperObj.getIcon(
                                                    viewmodel.selectedCategory
                                                )
                                            ),
                                            contentDescription = it.category.name,
                                            tint = MaterialTheme.colorScheme.onPrimary,
                                            modifier = Modifier
                                                .size(48.dp)
                                                .padding(6.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(9.dp))
                                    Text(
                                        text = it.title,
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                    )
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = if (it.credit) "Credit" else "Debit",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                    Text(
                                        text = "\u20b9 ${it.amount.toMoneyFormat()}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                                Spacer(modifier = Modifier.size(21.dp))
                                Text(
                                    text = it.description,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.W400,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(100.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "Your Spend Was Categorised",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 21.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.size(12.dp))
                        Text(
                            text = "Tap to change",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W300
                        )
                        Spacer(modifier = Modifier.size(18.dp))
                        CategorySmallChoice(
                            category = it.category,
                            viewmodel = viewmodel,
                            scaffoldState = scaffoldState,
                            scope = scope
                        )
                    }
                    Spacer(modifier = Modifier.size(100.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            modifier = Modifier.width(50.dp),
                            enabled = it.potId == null,
                            onClick = {
                                navController.navigateUp()
                                Toast.makeText(
                                    context,
                                    "Expense deleted successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                viewmodel.deleteExpense(it)
                            },
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Icon(imageVector = Icons.Outlined.Delete, contentDescription = "delete")
                        }

                        Button(
                            modifier = Modifier.width(50.dp),
                            enabled = it.potId == null,
                            onClick = {
                                navController.navigate("edit_transaction")
                            },
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Icon(imageVector = Icons.Outlined.Edit, contentDescription = "edit")
                        }

                        Button(
                            modifier = Modifier.width(200.dp),
                            enabled = viewmodel.selectedCategory != it.category,
                            onClick = {
                                val newExpense = it.copy(
                                    category = viewmodel.selectedCategory
                                )
                                viewmodel.updateExpense(it, newExpense)
                                viewmodel.selectedCategory = Category.Miscellaneous
                                navController.navigateUp()

                            }
                        ) {
                            Text(text = "Save")
                        }
                    }
                }
            }
        }
    }
}

fun getNextUniqueIndices(currentIndex: Int, size: Int): Pair<Int, Int> {
    val nextIndex1 = (currentIndex + 1) % size
    val nextIndex2 = (currentIndex + 2) % size
    return Pair(nextIndex1, nextIndex2)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySmallChoice(
    category: Category,
    viewmodel: FinanceViewmodel,
    scaffoldState: BottomSheetScaffoldState,
    scope: CoroutineScope
) {
    val index = category.ordinal
    val indices = getNextUniqueIndices(index, Category.entries.size)


    Row {

        CategoriesDescItem(
            item = Category.entries[index], img = 0, txt = "", viewmodel = viewmodel
        ) {
            viewmodel.selectedCategory = Category.entries[index]
        }

        CategoriesDescItem(
            item = Category.entries[indices.first], img = 0, txt = "", viewmodel = viewmodel
        ) {
            viewmodel.selectedCategory = Category.entries[indices.first]
        }
        CategoriesDescItem(
            item = Category.entries[indices.second], img = 0, txt = "", viewmodel = viewmodel
        ) {
            viewmodel.selectedCategory = Category.entries[indices.second]
        }

        CategoriesDescItem(
            item = null, img = R.drawable.finance_plus, txt = "More", viewmodel = viewmodel
        ) {
            scope.launch {
                scaffoldState.bottomSheetState.expand()
            }
        }
    }
}

@Composable
fun CategoriesDescItem(
    item: Category?, img: Int, txt: String, viewmodel: FinanceViewmodel, onClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .border(
                    2.dp,
                    color = if (item != null && viewmodel.selectedCategory == item) Color.Green else Color.Transparent,
                    shape = RoundedCornerShape(12.dp)
                )
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .clickable {
                    onClick()
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = if (item != null) HelperObj.getIcon(item) else img),
                contentDescription = item?.name,
                tint = Color.Black,
                modifier = Modifier
                    .size(48.dp)
                    .padding(6.dp)
            )
        }
        Text(
            text = item?.toString() ?: txt,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}