package com.example.coinlog.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coinlog.data.Category
import com.example.coinlog.data.HelperObj

@Composable
fun CategoriesPage(modifier: Modifier = Modifier) {
    Scaffold {
        Box(modifier = Modifier
            .padding(it)
            .fillMaxSize()) {
            val a = Category.entries.toTypedArray()
            LazyVerticalGrid(columns = GridCells.Fixed(3)) {
                items(a) { item ->
                    CategoriesPageItem(item = CategoriesContent(item))
                }
            }
        }
    }
}


@Composable
fun CategoriesPageItem(item: CategoriesContent) {
    Column(
        modifier = Modifier.padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
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
                    .size(60.dp)
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