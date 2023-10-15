package com.example.quran.presentation.otherApps.names_of_allah

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quran.R
import com.example.quran.navigation.Screen
import com.example.quran.others.AyahPrettyTextTransformer
import com.example.quran.others.Constants
import com.example.quran.others.isItInDarkTheme
import com.example.quran.ui.theme.borderColor
import com.example.quran.ui.theme.noto_arabic_font
import com.example.quran.ui.theme.primaryColor
import com.example.quran.ui.theme.thulth_font
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager


@OptIn(ExperimentalPagerApi::class)
@Composable
fun NamesOfAllahScreen(
    navController: NavController,
    namesViewModel: NamesOfAllahViewModel = hiltViewModel()
) {

    val allNames = namesViewModel.allNames.value

    LaunchedEffect(key1 = true) {
        namesViewModel.getAllNamesofAllah()
    }

    Scaffold(topBar = {
        Row(verticalAlignment = Alignment.CenterVertically , modifier = Modifier
            .fillMaxWidth()
            .padding(top = 18.dp, start = 18.dp)) {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_forward),
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "أسماء الله الحسنى", fontSize = 24.sp, fontFamily = noto_arabic_font)
        }
    }) {
        it
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (allNames.isNotEmpty()) {
                HorizontalPager(count = Constants.NAMES_OF_ALLAH_NUMBER) { index ->
                    val nameDescription =
                        AyahPrettyTextTransformer.removeUnsupportedChars(allNames[index].description)
                    NamesOfAllahCard(
                        name = allNames[index].name,
                        description = nameDescription
                    )
                }
            } else {
                CircularProgressIndicator()
            }

        }

    }

}


@Composable
fun NamesOfAllahCard(
    name: String = "المُهَيْمِنُ",
    description: String = "ورد في قوله تعالى: { هو الله الذي لا إله إلا هو الملك القدوس السلام المؤمن المهيمن }(الحشر:23), ومعناه: مأخوذ من الهيمنة, وهي السيطرة على الشيء بقهره, فالله قاهر لخلقه لا يخرج أحد عن إرادته الكونية, وسلطانه القدري فما شاء الله كان, وما لم يشأ لم يكن.\n",
) {
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(primaryColor, primaryColor, primaryColor, Color(0xFF2196F3)),
    )

    Card(
        elevation = 0.dp,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        border = BorderStroke(width = 1.dp, color = borderColor),
        backgroundColor = if (MaterialTheme.colors.isLight) Color.White else MaterialTheme.colors.surface
    ) {

        Box(contentAlignment = Alignment.Center) {

            if(!isItInDarkTheme()){
                Image(
                    painter = painterResource(id = R.drawable.background_noa_top), // Replace with your image resource
                    contentDescription = null, // Provide a meaningful description
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .size(120.dp),// Adjust the size as needed
                    contentScale = ContentScale.Crop
                )

                Image(
                    painter = painterResource(id = R.drawable.background_noa_bottom), // Replace with your image resource
                    contentDescription = null, // Provide a meaningful description
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .width(120.dp), // Adjust the size as needed
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = name,
                    fontSize = 56.sp,
                    fontFamily = thulth_font,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .padding(vertical = 24.dp)
                        .textBrush(gradientBrush),
                )
                Text(
                    text = description,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    textAlign = TextAlign.Justify
                )

                Spacer(modifier = Modifier.height(24.dp))

            }

        }

    }

}


fun Modifier.textBrush(brush: Brush) = this
    .graphicsLayer(alpha = 0.99f)
    .drawWithCache {
        onDrawWithContent {
            drawContent()
            drawRect(brush, blendMode = BlendMode.SrcAtop)
        }
    }