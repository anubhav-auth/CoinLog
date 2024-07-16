package com.example.coinlog.presentation


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coinlog.data.FinanceViewmodel

@Composable
fun BottomMenu(
    modifier: Modifier = Modifier,
    items: List<BottomMenuContent>,
    viewmodel: FinanceViewmodel
) {

    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(horizontal = 18.dp)
            .padding(bottom = 18.dp)
            .clip(RoundedCornerShape(36.dp))
            .background(Color.DarkGray)
            .padding(top = 9.dp)
            .fillMaxWidth()

    ) {
        items.forEachIndexed { index, item2 ->
            BottomMenuItem(
                item = item2,
                isSelected = index == viewmodel.selectedItemIndex
            ) {
                viewmodel.selectedItemIndex = index
            }
        }
    }
}


@Composable
fun BottomMenuItem(
    item: BottomMenuContent,
    isSelected: Boolean = false,
    activeTextColour: Color = Color.White,
    inactiveTextColor: Color = Color.LightGray,
    onItemClick: () -> Unit
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }
    Box(
        modifier = Modifier.clickable(
            interactionSource = interactionSource,
            indication = null
        ) { onItemClick() }, contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier) {
            Icon(
                painter = painterResource(id = item.iconId),
                contentDescription = null,
                tint = if (isSelected) activeTextColour else inactiveTextColor,
                modifier = Modifier
                    .size(item.iconSize)
                    .align(Alignment.CenterHorizontally)
            )
//            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = item.title,
                color = if (isSelected) activeTextColour else inactiveTextColor,
                fontSize = 10.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

    }
}

data class BottomMenuContent(
    val title: String, val iconId: Int, val iconSize: Dp = 24.dp
)

@Preview
@Composable
fun Pre(modifier: Modifier = Modifier) {

}