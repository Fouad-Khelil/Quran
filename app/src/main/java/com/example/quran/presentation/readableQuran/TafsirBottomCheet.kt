package com.example.quran.presentation.readableQuran

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.quran.R
import com.example.quran.presentation.sharedUi.ChipGroup
import com.example.quran.ui.theme.*


@Composable
fun TafsirBottomCheet(
    sora: String,
    ayahRange: String,
    ayah: String,
    tafsir: String,
    wordsExplanation: String,
    navController: NavController
) {

    var isAyaPlayed by remember { mutableStateOf(false) }
    var chipGroupIndex by remember { mutableStateOf(0) }

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp)
            .padding(bottom = 18.dp)

    ) {
        Spacer(Modifier.height(12.dp))
        Surface(
            modifier = Modifier
                .height(4.dp)
                .width(64.dp)
                .align(CenterHorizontally)
                .clip(CircleShape),
            color = secondaryBackgroundColor
        ) {}
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row {
                Text(text = "$sora    ", color = MaterialTheme.colors.primary, fontSize = 20.sp)
                Text(text = ayahRange, color = MaterialTheme.colors.onSurface, fontSize = 18.sp)
            }
            Icon(
                painter = painterResource(id =if (!isAyaPlayed) R.drawable.ic_play else R.drawable.ic_round_pause_24),
                contentDescription = "",
                tint = MaterialTheme.colors.primary ,
                modifier = Modifier.clickable {
                    isAyaPlayed =!isAyaPlayed
                }
            )
        }
        Spacer(Modifier.height(12.dp))
        Divider()
        Spacer(Modifier.height(12.dp))
        Text(text = ayah, fontSize = 18.sp)
        Spacer(Modifier.height(12.dp))
        Divider()
        Spacer(Modifier.height(12.dp))
        ChipGroup(
            chipContent = listOf("التفسير", "شرح الكلمات"),
            PaddingValues(horizontal = 0.dp) ,
            onChipClick =  { index ->
                chipGroupIndex = index
            }
        )

        Spacer(Modifier.height(12.dp))
        Text(
            text = if(chipGroupIndex == 0)tafsir else "شرح الكلمات غير متوفر حاليا ",
            fontSize = 14.sp,
            fontFamily = noto_arabic_font,
            color = MaterialTheme.colors.onSurface ,
        )
    }
}