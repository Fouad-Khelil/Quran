package com.example.quran.presentation.otherApps

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.quran.R
import com.example.quran.navigation.Screen
import com.example.quran.ui.theme.primaryColor

@Composable
fun OtherAppsScreen(navController: NavController) {

    val context = LocalContext.current
    OvalShape()
    Text(
        text = "أدوات أخرى",
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 18.dp),
        textAlign = TextAlign.Center ,
        color = Color.White ,
        fontSize = 24.sp ,
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(start = 18.dp, top = 120.dp, end = 18.dp, bottom = 18.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        items(appsList) { appItem ->
            OtherAppItem(appItem.icon, appItem.name, appItem.route) { route ->
                if (route ==Screen.NotificationScreen.route) {
                    Toast.makeText(context,"التنبيهات غير متوفرة الان ,انتظر التحديثات" , Toast.LENGTH_SHORT).show()
                    return@OtherAppItem
                }
                navController.navigate(route)
            }
        }
    }


//    Button(onClick = {
//        navController.navigate(Screen.QiblaCompassScreen.route)
//    }) {
//        Text(text = "navigate to compass")
//    }
}

@Composable
fun OtherAppItem(
    @DrawableRes icon: Int,
    name: String,
    route: String,
    onClickItem: (String) -> Unit
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClickItem(route)
            },
    ) {

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colors.surface)
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = "",
                modifier = Modifier.size(42.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = name, fontSize = 16.sp)
    }
}


@Composable
fun OvalShape() {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        val screenWidth = size.width

        val ovalWidth = screenWidth
        val ovalHeight = 160.dp.toPx()

        val offsetX = 0f
        val offsetY = -(ovalHeight / 2)

        drawOval(
            color = primaryColor,
            size = Size(width = ovalWidth, height = ovalHeight),
            topLeft = Offset(x = offsetX, y = offsetY)
        )
    }
}


data class AppItem(
    val icon: Int,
    val name: String,
    val route: String,
)

val appsList = listOf(
    AppItem(R.drawable.ic_prayer_rug, "القبلة", Screen.QiblaCompassScreen.route),
    AppItem(R.drawable.ic_names_of_allah, "أسماء الله الحسنى", Screen.NamesOfAllahScreen.route),
    AppItem(R.drawable.ic_book, "حديث", Screen.HadithScreen.route),
    AppItem(R.drawable.ic_notification, "التنبيهات", Screen.NotificationScreen.route),
    AppItem(R.drawable.ic_about_us, "حول التطبيق", Screen.AboutTheAppScreen.route),
    AppItem(R.drawable.ic_settings, "الاعدادات", Screen.SettingsScreen.route),
)