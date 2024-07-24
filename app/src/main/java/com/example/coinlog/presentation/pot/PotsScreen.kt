package com.example.coinlog.presentation.pot

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.coinlog.data.Category
import com.example.coinlog.data.FinanceViewmodel
import com.example.coinlog.data.HelperObj
import com.example.coinlog.data.Pot
import com.example.coinlog.presentation.homeScreen.toMoneyFormat
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun PotsScreen(viewmodel: FinanceViewmodel, navController: NavController) {
    viewmodel.loadAllPots()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("new_pot_add")
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
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 12.dp)
        ) {

            PotDisplay(viewmodel = viewmodel, navController = navController)

        }
    }
}

@Composable
fun PotDisplay(viewmodel: FinanceViewmodel, navController: NavController) {
    val allPots by viewmodel.allPots.collectAsState()
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        contentPadding = PaddingValues(bottom = 135.dp)
    ) {
        items(allPots) { item ->
            PotCard(item = item, viewmodel = viewmodel, navController = navController)
        }
    }
}

@Composable
fun PotCard(item: Pot, viewmodel: FinanceViewmodel, navController: NavController) {

    Row(
        modifier = Modifier
            .padding(bottom = 6.dp)
            .clip(RoundedCornerShape(15.dp))
            .clickable {
                navController.navigate("pot_details/${item.id}")
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
                    painter = painterResource(id = HelperObj.getIcon(Category.Pot)),
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
            }
        }
        val formater = SimpleDateFormat("EEE, dd MMM")
        val date = Date(item.dateAdded)

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "\u20b9 ${item.amount.toMoneyFormat()}",
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = formater.format(date),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}