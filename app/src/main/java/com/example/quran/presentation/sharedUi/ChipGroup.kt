package com.example.quran.presentation.sharedUi

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.quran.others.isItInDarkTheme
import com.example.quran.ui.theme.secondaryBackgroundColor

@Composable
fun ChipItem(
    item :Int  ,
    text: String,
    isSelected: Boolean,
    onClick: (Int) -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick(item)
            },
        color = if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
        contentColor = if (isSelected) Color.White else if(isItInDarkTheme()) Color.White else Color.Black,
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}


@Composable
fun ChipGroup(
    chipContent: List<String>,
    padding : PaddingValues =PaddingValues(horizontal = 18.dp) ,
    onChipClick: (Int) -> Unit
) {
    var selectedIndex by remember { mutableStateOf(0) }
    LazyRow(
        contentPadding = padding,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(chipContent ){ index , text ->
            ChipItem(
                item = index,
                text = text,
//                isSelected = navController.currentBackStackEntry?.destination?.route == text
                isSelected = selectedIndex == index ,
                onClick = {
                    selectedIndex = it
                    onChipClick(it)
                    //to do
                }
            )
        }
    }

}