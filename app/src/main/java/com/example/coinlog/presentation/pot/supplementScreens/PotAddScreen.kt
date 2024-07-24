package com.example.coinlog.presentation.pot.supplementScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.coinlog.R
import com.example.coinlog.data.FinanceViewmodel
import com.example.coinlog.data.Pot

@Composable
fun PotAddScreen(viewmodel: FinanceViewmodel, navController: NavController) {
    val currentSummary by viewmodel.currentSummary.collectAsState()

    var title by remember {
        mutableStateOf("Savings")
    }
    var amountText by remember {
        mutableStateOf("1000.00")
    }
    var textFieldEnabled by remember {
        mutableStateOf(false)
    }

    Column(modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = R.drawable.finance_piggy_bank),
            contentDescription = "",
            modifier = Modifier.size(200.dp)
        )
        Spacer(modifier = Modifier.size(21.dp))
        Row {
            TextField(
                value = title,
                onValueChange = {
                    title = it
                },
                singleLine = true,
                maxLines = 1,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        textFieldEnabled = false
                    }
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                enabled = textFieldEnabled
            )
            if (!textFieldEnabled) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "",
                    modifier = Modifier.clickable { textFieldEnabled = true })
            }
        }
        Spacer(modifier = Modifier.size(21.dp))
        Text(
            text = "Enter amount to save",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
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
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent
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
                imeAction = ImeAction.Done
            )
        )
        val newPot = Pot(
            title = title,
            amount = if (amountText.isNotBlank()) amountText.toDouble() else 0.0,
            dateAdded = System.currentTimeMillis()
        )
//        Log.d("mytag", newPot.toString())
        Button(
            onClick = {
                viewmodel.savePot(
                    newPot
                )
                navController.navigateUp()
            },
            enabled = (if (amountText.isNotBlank()) amountText.toDouble() else 0.0) <= currentSummary.balance
        ) {
            Text(text = "Save")
        }
    }
}