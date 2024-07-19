package com.example.coinlog.presentation.homeScreen.supplementScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.coinlog.R
import com.example.coinlog.data.FinanceViewmodel

@Composable
fun EditData(
    navController: NavController,
    viewmodel: FinanceViewmodel,
) {
    val currentExpense by viewmodel.transactionDataFetched.collectAsState()
    currentExpense?.let {
        Scaffold { paddingvalues ->
            var titleText by remember {
                mutableStateOf(it.title)
            }
            var descriptionText by remember {
                mutableStateOf(it.description)
            }
            var amountText by remember {
                mutableStateOf(it.amount.toString())
            }
            val focusRequest2 = remember {
                FocusRequester()
            }
            val focusRequest3 = remember {
                FocusRequester()
            }
            val keyboardController = LocalSoftwareKeyboardController.current

            Column(
                modifier = Modifier
                    .padding(paddingvalues)
                    .fillMaxSize()
            ) {
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


                val newExpense = it.copy(
                    title = titleText,
                    description = descriptionText,
                    amount = if (amountText.isNotBlank()) amountText.toDouble() else 0.0
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
                        },
                        modifier = Modifier.width(buttonWidth)
                    ) {
                        Text(text = "Cancel")
                    }
                    Button(
                        onClick = {
                            viewmodel.updateExpense(it, newExpense)
                            navController.navigateUp()
                            titleText = ""
                            descriptionText = ""
                            amountText = ""
                        },
                        modifier = Modifier.width(buttonWidth)
                    ) {
                        Text(text = "Save")
                    }
                }

            }
        }
    }
}