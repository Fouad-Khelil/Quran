package com.example.quran.presentation.otherApps.settings

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.quran.R
import com.example.quran.data.repository.DataStoreRepository
import com.example.quran.navigation.Screen
import com.example.quran.others.Constants
import com.example.quran.presentation.otherApps.AboutTheApp.AppHeader
import com.example.quran.presentation.otherApps.IosSwitcher
import com.example.quran.presentation.readableQuran.ThemeGroup
import com.example.quran.ui.theme.noto_arabic_font
import com.example.quran.ui.theme.thirdBackgroundColor


@Composable
fun SettingScreen(
    navController: NavController,
    onThemeUpdated: (Int) -> Unit,
) {

    val context = LocalContext.current
    var fontSize by remember { mutableStateOf(Constants.INITIAL_FONT_SIZE) }
    var isEnabled by remember { mutableStateOf(DataStoreRepository(context).getAppBarSetting()) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedMethod by remember { mutableStateOf(DataStoreRepository(context).getCalculationMethod()) }

    val scrollState = rememberScrollState()

    Column(
        Modifier
            .verticalScroll(scrollState)
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AppHeader(navController, R.string.settings)
        Text(
            text = "الثيمات",
            modifier = Modifier.fillMaxWidth(),
            fontFamily = noto_arabic_font,
        )
        ThemeGroup(onChangeTheme = onThemeUpdated)

        Spacer(modifier = Modifier.height(8.dp))

        FontSetting(fontSize) {
            fontSize = it
            DataStoreRepository(context).setFontSize(it)
        }

        QuranAppBarSetting(isEnabled = isEnabled) {
            isEnabled = !isEnabled
            DataStoreRepository(context).setAppBarSetting(isEnabled)
        }

        SettingItem(text = R.string.location , expendedText = "الجزائر العاصمة") {

        }

        SettingItem(
            text = R.string.method_of_calculation,
            expendedText = selectedMethod
        ) {
            showDialog = true
        }

    }


    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Column(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colors.surface)
                    .padding(18.dp)
            ) {
                for (item in Constants.prayerTimesCalculationMethods) {
                    Text(text = item,
                        color = if (item == selectedMethod) MaterialTheme.colors.primary else MaterialTheme.colors.onBackground,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clickable {
                                selectedMethod = item
                                DataStoreRepository(context).setCalculationMethod(item)
                                showDialog = false
                            })
                    Divider()
                }
            }
        }
    }

}


@Composable
fun QuranAppBarSetting(
    isEnabled: Boolean,
    onClick: (Boolean) -> Unit
) {
    Card(
        elevation = 0.dp,
        shape = MaterialTheme.shapes.medium,
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "اخفاء شريط صفحة القران",
                fontFamily = noto_arabic_font,
            )
            IosSwitcher(isEnabled = isEnabled) {
                onClick(it)
            }
        }
    }


}

@Composable
private fun FontSetting(fontSize: Float, onFontChanged: (Float) -> Unit) {
    Card(
        elevation = 0.dp,
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            Text(
                text = "حجم الخط",
                modifier = Modifier.fillMaxWidth(),
                fontFamily = noto_arabic_font,

                )
            Spacer(modifier = Modifier.height(8.dp))

            FontSlider(fontSize) {
                onFontChanged(it)
            }
        }
    }

}


@Composable
private fun FontSlider(fontSize: Float, onFontChange: (Float) -> Unit) {
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
                inactiveTrackColor = thirdBackgroundColor
            ),
            onValueChange = {
                onFontChange(it)
            },
            valueRange = 22f..36f,
            steps = 7,
        )
    }
}

sealed class IconType {
    data class Vector(val imageVector: ImageVector) : IconType()
    data class Painterr(val painter: Painter) : IconType()
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingItem(
    @StringRes text: Int,
    expendedText: String? = null,
    icon: IconType? = null,
    isTintApplied: Boolean = true,
    onClick: () -> Unit = {}
) {
    Card(
        elevation = 0.dp,
//        backgroundColor = secondaryBackgroundColor,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            onClick()
        }
    ) {

        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    when (icon) {
                        is IconType.Vector -> {
                            Icon(
                                imageVector = icon.imageVector,
                                contentDescription = null,
                                tint = MaterialTheme.colors.primary
                            )
                        }

                        is IconType.Painterr -> {
                            Icon(
                                painter = icon.painter,
                                contentDescription = null,
                                tint = if (isTintApplied) MaterialTheme.colors.primary else Color.Unspecified
                            )
                        }

                        null -> {
                            // Handle the case where no icon is provided
                        }
                    }
                    Spacer(modifier = Modifier.width(if (icon == null) 8.dp else 12.dp))

                    Text(
                        text = stringResource(id = text),
                        fontFamily = noto_arabic_font
                    )
                }
                Icon(
                    painter = painterResource(id = R.drawable.ic_round_navigate_next_24),
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary
                )
            }

            if (expendedText != null) {
                Divider(Modifier.padding(vertical = 8.dp))
                Text(
                    text = expendedText,
                    fontFamily = noto_arabic_font,
                    fontSize = 14.sp,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

        }
    }
}