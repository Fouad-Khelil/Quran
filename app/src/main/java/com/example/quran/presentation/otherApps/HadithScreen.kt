package com.example.quran.presentation.otherApps

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.quran.R
import com.example.quran.data.repository.DataStoreRepository
import com.example.quran.others.AyahPrettyTextTransformer
import com.example.quran.others.Constants
import com.example.quran.others.isItInDarkTheme
import com.example.quran.presentation.otherApps.names_of_allah.NamesOfAllahCard
import com.example.quran.presentation.otherApps.names_of_allah.textBrush
import com.example.quran.ui.theme.borderColor
import com.example.quran.ui.theme.noto_arabic_font
import com.example.quran.ui.theme.thulth_font
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HadithScreen(navController: NavController) {
    val context = LocalContext.current

    val pagerState = rememberPagerState(initialPage = DataStoreRepository(context).getLastHadithIndex()) // to change

    DisposableEffect(key1 = Unit){
        onDispose {
            DataStoreRepository(context).saveLastHadithIndex(pagerState.currentPage)
        }
    }

    Scaffold(topBar = {
        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .fillMaxWidth()
                .padding(top = 18.dp, start = 18.dp)
        ) {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_forward),
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "الأحاديث-الأربعون النووية",
                fontSize = 24.sp,
                fontFamily = noto_arabic_font
            )
        }
    }) {
        HorizontalPager(count = Constants.NAWAWI_HADITH.size , state = pagerState) { index ->

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Card(
                    elevation = 0.dp,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .fillMaxWidth()
                        .padding(24.dp),
                    border = BorderStroke(width = 1.dp, color = borderColor),
                    backgroundColor = if (MaterialTheme.colors.isLight) Color.White else MaterialTheme.colors.surface
                ) {
                    Text(
                        text = Constants.NAWAWI_HADITH[index],
                        fontSize = 18.sp,
                        modifier = Modifier.padding(horizontal = 18.dp , vertical = 24.dp),
                        textAlign = TextAlign.Justify
                    )
                }

            }

        }
    }

}
