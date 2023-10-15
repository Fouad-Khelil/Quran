package com.example.quran.presentation.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quran.R
import com.example.quran.ui.theme.hafs_uthmanic_font

@Composable
fun DecorationLastViewItem(
    sora: String,
    hizbOrMokri: String,
    @DrawableRes icon: Int,
    @StringRes lastAction: Int,
    modifier: Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium),
            contentScale = ContentScale.FillWidth,
            painter = painterResource(id = R.drawable.img_decoration),
            contentDescription = "decoration"
        )
        Column(horizontalAlignment = Alignment.Start, modifier = Modifier.padding(start = 12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = icon),
                    tint = Color.White,
                    contentDescription = "icon last view decoration"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(id = lastAction),
                    fontSize = 20.sp,
                    fontFamily = hafs_uthmanic_font,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "$hizbOrMokri - $sora",
                fontSize = 16.sp,
                fontFamily = hafs_uthmanic_font,
                color = Color.White
            )
        }

    }
}

@Composable
fun DecorationMain(
    lastReciter : String , 
    lastReadingSurah :String,
    lastListeningSurah :Int ,
    onClickLastReading: () -> Unit,
    onClickLastListening: () -> Unit
) {
    Row(Modifier.padding(horizontal = 18.dp, vertical = 18.dp)) {
        DecorationLastViewItem(
            modifier = Modifier
                .weight(1f)
                .clickable {
                    onClickLastReading()
                },
            sora = lastReadingSurah,
            hizbOrMokri = "السورة :",
            lastAction = R.string.last_reading,
            icon = R.drawable.ic_book_main
        )
        Spacer(modifier = Modifier.width(8.dp))
        DecorationLastViewItem(
            modifier = Modifier
                .weight(1f)
                .clickable {
                    onClickLastListening()
                },
            sora = "السورة رقم $lastListeningSurah",
            hizbOrMokri = lastReciter,
            lastAction = R.string.last_listening,
            icon = R.drawable.ic_play_outlined
        )
    }
}

