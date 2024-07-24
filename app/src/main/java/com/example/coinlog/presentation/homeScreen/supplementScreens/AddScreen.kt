package com.example.coinlog.presentation.homeScreen.supplementScreens

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.coinlog.R
import com.example.coinlog.data.Category
import com.example.coinlog.data.Expenses
import com.example.coinlog.data.FinanceViewmodel
import com.example.coinlog.data.HelperObj
import com.example.coinlog.presentation.homeScreen.CategoriesContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(
    paddingValues: PaddingValues,
    viewmodel: FinanceViewmodel,
    navController: NavController,
    scope: CoroutineScope
) {
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden,
            skipHiddenState = false
        )
    )

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
                    val a = Category.entries.toTypedArray()
                    val categoriesToSkip = listOf(Category.Pot, Category.Warning)
                    val filteredCategory = a.filter { it !in categoriesToSkip }
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
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TopChoice(viewmodel = viewmodel)
            Spacer(modifier = Modifier.size(21.dp))
            CategorySheetLaunch(scaffoldState, scope, viewmodel)
            DataEntry(navController = navController, viewmodel = viewmodel)
        }
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
                color = if (0 == viewmodel.selectedItemIndexInEx) MaterialTheme.colorScheme.onTertiary else Color.Gray,
                fontWeight = if (0 == viewmodel.selectedItemIndexInEx) FontWeight.Bold else FontWeight.Normal,
                fontSize = if (0 == viewmodel.selectedItemIndexInEx) 21.sp else 15.sp,
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
                color = if (1 == viewmodel.selectedItemIndexInEx) MaterialTheme.colorScheme.onTertiary else Color.Gray,
                fontWeight = if (1 == viewmodel.selectedItemIndexInEx) FontWeight.Bold else FontWeight.Normal,
                fontSize = if (1 == viewmodel.selectedItemIndexInEx) 21.sp else 15.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySheetLaunch(
    scaffoldState: BottomSheetScaffoldState,
    scope: CoroutineScope,
    viewmodel: FinanceViewmodel
) {
    Box(
        modifier = Modifier

            .padding(horizontal = 12.dp, vertical = 21.dp)
            .clip(RoundedCornerShape(21.dp))
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary)
            .clickable {
                scope.launch {
                    scaffoldState.bottomSheetState.expand()
                }
            }
    ) {
        CategoriesChoiceItem(item = CategoriesContent(viewmodel.selectedCategory))
    }

}

@Composable
fun CategoriesChoiceItem(item: CategoriesContent) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
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
                        .size(42.dp)
                        .padding(6.dp)
                )
            }
            Spacer(modifier = Modifier.size(9.dp))
            Text(
                text = item.category.toString(),
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontWeight = FontWeight.Bold
            )
        }
        Icon(
            imageVector = Icons.Filled.KeyboardArrowUp,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.size(27.dp)
        )
    }
}

@Composable
fun DataEntry(
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
    val focusRequest2 = remember {
        FocusRequester()
    }
    val focusRequest3 = remember {
        FocusRequester()
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(modifier = Modifier) {
        TextField(
            value = amountText,
            onValueChange = {
                amountText = it.split("[\\s,\\-]+".toRegex()).joinToString("")
            },
            singleLine = true,
            maxLines = 1,
            placeholder = {
                Text(text = "Amount")
            },
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 21.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(15.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.finance_rupee),
                    contentDescription = "",
                    modifier = Modifier.size(24.dp)
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusRequest2.requestFocus()
                }
            )
        )
        TextField(
            value = titleText,
            onValueChange = {
                titleText = it
            },
            singleLine = true,
            maxLines = 1,
            placeholder = {
                Text(text = "Title")
            },
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 21.dp)
                .fillMaxWidth()
                .focusRequester(focusRequest2),
            shape = RoundedCornerShape(15.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusRequest3.requestFocus()
                }
            )
        )
        TextField(
            value = descriptionText,
            onValueChange = {
                descriptionText = it
            },
            maxLines = 5,
            placeholder = {
                Text(text = "Description")
            },
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 21.dp)
                .fillMaxWidth()
                .fillMaxSize(0.4f)
                .focusRequester(focusRequest3),
            shape = RoundedCornerShape(15.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            )
        )


        val newExpense = Expenses(
            title = titleText,
            description = descriptionText,
            category = viewmodel.selectedCategory,
            credit = viewmodel.selectedItemIndexInEx == 0,
            amount = if (amountText.isNotBlank()) amountText.toDouble() else 0.0,
            dateAdded = System.currentTimeMillis()
        )

        Spacer(modifier = Modifier.size(60.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 21.dp)
                .fillMaxWidth()
        ) {
            val buttonWidth = 150.dp
            Button(
                onClick = {
                    navController.navigateUp()
                    titleText = ""
                    descriptionText = ""
                    amountText = ""
                    viewmodel.selectedCategory = Category.Miscellaneous
                    viewmodel.selectedItemIndexInEx = 1
                },
                modifier = Modifier.width(buttonWidth)
            ) {
                Text(text = "Cancel")
            }
            Button(
                onClick = {
                    viewmodel.saveExpense(newExpense)
                    navController.navigateUp()
                    titleText = ""
                    descriptionText = ""
                    amountText = ""
                    viewmodel.selectedCategory = Category.Miscellaneous
                    viewmodel.selectedItemIndexInEx = 1
                },
                modifier = Modifier.width(buttonWidth)
            ) {
                Text(text = "Save")
            }
        }

    }
}

@Composable
fun CategoriesSheetItem(item: CategoriesContent, viewmodel: FinanceViewmodel) {
    Column(
        modifier = Modifier
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier

                .clickable {
                    viewmodel.selectedCategory = item.category
                }
                .clip(RoundedCornerShape(12.dp))
                .border(
                    2.dp,
                    color = if (viewmodel.selectedCategory == item.category) Color.Green else Color.Transparent,
                    shape = RoundedCornerShape(12.dp)
                )
                .background(MaterialTheme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = HelperObj.getIcon(item.category)),
                contentDescription = item.category.name,
                tint = Color.Black,
                modifier = Modifier
                    .size(if (viewmodel.selectedCategory == item.category) 51.dp else 48.dp)
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