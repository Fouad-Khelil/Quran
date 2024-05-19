package com.example.quran.presentation.readableQuran

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quran.R
import com.example.quran.data.repository.DataStoreRepository
import com.example.quran.others.Constants
import com.example.quran.others.isItInDarkTheme
import com.example.quran.ui.theme.noto_arabic_font
import com.example.quran.ui.theme.onSecondaryBackground
import com.example.quran.ui.theme.onSecondaryDarkBackground
import com.example.quran.ui.theme.secondaryBackgroundColor


@Composable
fun SettingBottomSheet(
    fontSize: Float,
    brightness: Float,
    onChangeFontSize: (Float) -> Unit = {},
    onChangeBrightness: (Float) -> Unit = {},
    onChangeTheme: (Int) -> Unit = {},
) {

    Column(
        modifier = Modifier
            .padding(16.dp)
            .padding(bottom = 24.dp),
        Arrangement.spacedBy(16.dp)
    ) {

        Box(
            Modifier
                .clip(CircleShape)
                .background(secondaryBackgroundColor)
                .align(CenterHorizontally)
                .size(width = 64.dp, height = 6.dp)
        ) {}
        Divider(color = secondaryBackgroundColor)
        Text(
            text = "الثيمات",
            modifier = Modifier.fillMaxWidth(),
            fontFamily = noto_arabic_font,
        )
        ThemeGroup(onChangeTheme)
        Text(
            text = "حجم الخط",
            modifier = Modifier.fillMaxWidth(),
            fontFamily = noto_arabic_font,
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.ic_font_size),
                contentDescription = null,
                tint = Color(0xFF646464)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Slider(
                value = fontSize,
                modifier = Modifier.fillMaxWidth(),
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colors.primary,
                    activeTrackColor = MaterialTheme.colors.primary,
                    inactiveTrackColor = secondaryBackgroundColor
                ),
                onValueChange = {
                    onChangeFontSize(it)
                },
                valueRange = 22f..36f,
                steps = 7,
            )
        }
        Text(
            text = "السطوع",
            modifier = Modifier.fillMaxWidth(),
            fontFamily = noto_arabic_font,
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.salat_zhuhur),
                contentDescription = null,
                tint = Color(0xFF646464)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Slider(
                value = brightness,
                modifier = Modifier.fillMaxWidth(),
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colors.primary,
                    activeTrackColor = MaterialTheme.colors.primary,
                    inactiveTrackColor = secondaryBackgroundColor
                ),
                onValueChange = {
                    onChangeBrightness(it)
                },
//                valueRange = 0f..100f
            )
        }


    }
}


@Composable
fun ThemeGroup(
    onChangeTheme: (Int) -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
//        modifier = Modifier.fillMaxWidth().padding(18.dp)
    ) {
        val context = LocalContext.current

        val interactionSource = remember { MutableInteractionSource() }
        var theme by remember { mutableStateOf(DataStoreRepository(context).getTheme()) }
        ThemeGroupItem(
            themeImage = R.drawable.light_mode_vector,
            text = "نهاري",
            isSelected = theme == Constants.LIGHT_THEME,
            modifier = Modifier
                .weight(1f)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    theme = Constants.LIGHT_THEME
                    onChangeTheme(theme)
                },
        )
        ThemeGroupItem(
            themeImage = R.drawable.dark_mode_vector,
            text = "ليلي",
            isSelected = theme == Constants.DARK_THEME,
            modifier = Modifier
                .weight(1f)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    theme = Constants.DARK_THEME
                    onChangeTheme(theme)
                },
        )
        ThemeGroupItem(
            themeImage = R.drawable.system_mode_vector,
            text = "نظام",
            isSelected = theme == Constants.SYSTEM_THEME,
            modifier = Modifier
                .weight(1f)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    theme = Constants.SYSTEM_THEME
                    onChangeTheme(theme)
                },
        )

    }
}


@Composable
fun ThemeGroupItem(
    @DrawableRes themeImage: Int,
    text: String,
    isSelected: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(horizontalAlignment = CenterHorizontally, modifier = modifier) {
        Image(
            painter = painterResource(id = themeImage),
            contentDescription = null,
            modifier = Modifier.border(
                2.dp,
                color = if (isSelected) MaterialTheme.colors.primary else Color.Transparent,
                shape = RoundedCornerShape(8.dp, 8.dp, 4.dp, 4.dp)
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = text,
            fontFamily = noto_arabic_font,
            color = if (isSelected) MaterialTheme.colors.primary else Color(0xFF646464),
            fontSize = 14.sp
        )
    }

}


@Composable
fun SoraItem(
    soraOrder: Int,
    soraName: String,
    makiOrMadani: String,
    numberOfAya: Int,
    lastReading: String,
    icon: Int = R.drawable.ic_round_navigate_next_24,
    onClickPlayIcon: () -> Unit,
    onClickSora: () -> Unit
) {



    Column(
        modifier = Modifier
        .clickable {
            onClickSora()
        }
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.padding(top = 6.dp), contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_islamic_decoration),
                        contentDescription = "",
                        tint = MaterialTheme.colors.primary
                    )
                    Text(
                        text = soraOrder.toString(),
                        fontFamily = FontFamily.Default,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))

                Column(Modifier.align(Alignment.Top)) {
                    Text(
                        text = soraName,
                        fontSize = 24.sp,
                    )
                    Text(
                        text = "$makiOrMadani اياتها $numberOfAya",
                        fontSize = 10.sp,
                        fontFamily = noto_arabic_font,
                        color = if(isItInDarkTheme()) onSecondaryDarkBackground else onSecondaryBackground
                    )
                }
            }

            Icon(
                modifier = Modifier
                    .padding(top = 6.dp)
                    .clickable {
                        onClickPlayIcon()
                    },
                painter = painterResource(id = icon),
                contentDescription = "",
                tint = MaterialTheme.colors.primary
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp), color = MaterialTheme.colors.surface
        )
    }
}



//------------ preview------------------------

@Preview(showBackground = true)
@Composable
fun SoraItemPreview() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
//        SoraItem(
//            1, "البقرة", "مدنية", 286, "منذ 1 شهر"
//        ) {}
    }
}