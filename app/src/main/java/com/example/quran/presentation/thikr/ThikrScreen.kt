package com.example.quran.presentation.thikr

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quran.R
import com.example.quran.navigation.Screen
import com.example.quran.ui.theme.secondaryBackgroundColor


@Composable
fun ThikrCategoryItem(
    @DrawableRes icon: Int,
    @StringRes name: Int,
    iconColor: Color,
    navController: NavController
) {

    val nameCategory = stringResource(id = name)

    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(shape = MaterialTheme.shapes.medium)
            .clickable {
                navController.navigate(Screen.ThikrDetailScreen.createRoute(nameCategory))
            }
            .background(MaterialTheme.colors.surface)
            .padding(horizontal = 8.dp, vertical = 12.dp)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = "",
            tint = iconColor,
            modifier = Modifier.sizeIn(maxWidth = 30.dp, maxHeight = 30.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = stringResource(id = name), fontSize = 18.sp)
    }

}

@Composable
fun ThirkrScreen(
    navController: NavController ,
) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(18.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(thikrCategoryList) { thikrItem ->
            ThikrCategoryItem(thikrItem.icon, thikrItem.name, thikrItem.iconColor, navController)
        }
    }
}


//-----------------data -----------------------------
data class ThikrCategory(val icon: Int, val name: Int, val iconColor: Color)

val thikrCategoryList = listOf(
    ThikrCategory(
        icon = R.drawable.salat_dhuha,
        name = R.string.morning_prayers,
        iconColor = Color(0xffFD9637)
    ),
    ThikrCategory(
        icon = R.drawable.salat_isha, name = R.string.evening_prayers, iconColor = Color(0xff10B8DD)
    ),
    ThikrCategory(
        icon = R.drawable.salat_fadjr,
        name = R.string.waking_up_prayers,
        iconColor = Color(0xffFFE920)
    ),
    ThikrCategory(
        icon = R.drawable.ic_shalat,
        name = R.string.ablution_and_salat_prayers,
        iconColor = Color.Unspecified
    ),
    ThikrCategory(
        icon = R.drawable.ic_baseline_coffee_24,
        name = R.string.eating_and_drinking_prayers,
        iconColor = Color(0xFF351400)
    ),
    ThikrCategory(
        icon = R.drawable.ic_baseline_airplanemode_active_24,
        name = R.string.traveling_prayers,
        iconColor = Color(0xFF5C5855)
    ),
    ThikrCategory(
        icon = R.drawable.ic_round_home_24,
        name = R.string.home_prayers,
        iconColor = Color(0xFF2ED18E)
    ),
    ThikrCategory(
        icon = R.drawable.img_happy,
        name = R.string.joy_and_fear_prayers,
        iconColor = Color.Unspecified
    ),
    ThikrCategory(
        icon = R.drawable.img_kaaba,
        name = R.string.elhadj_and_elomra_prayers,
        iconColor = Color.Unspecified
    ),
    ThikrCategory(
        icon = R.drawable.img_shook_hands,
        name = R.string.dealing_and_etiquette,
        iconColor = Color.Unspecified
    ),
    ThikrCategory(
        icon = R.drawable.more, name = R.string.other_prayers, iconColor = Color(0xffFD9637)
    ),
)