package com.example.quran.presentation.splash

import android.util.Log
import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quran.navigation.ItemBottomNav
import com.example.quran.navigation.Screen
import com.example.quran.ui.theme.primaryColor
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    splashViewModel: SplashViewModel = hiltViewModel(),
    navController: NavController,
    scaledegree: Float = 1f,
    painter: Painter
) {
//    val scale = remember {
//        Animatable(0f)
//    }
//    var isLogoVisible by remember { mutableStateOf(true) }

    val isdbBuilt by splashViewModel.isdbBuilt.collectAsState()
    val context = LocalContext.current


    LaunchedEffect(key1 = isdbBuilt) {
//        if (isdbBuilt != null) {
        Log.d("datasotre", "inside: ")
        if (!splashViewModel.getIsDbBuilt()) {
            splashViewModel.loadDb(context)
        } else {
//            splashViewModel.saveOnLoadQuranState(false)
            Log.d("datastore", "insideme${splashViewModel.getIsDbBuilt()} ")
            if (splashViewModel.getIsFirstTime()) {
                splashViewModel.setIsFirstTime(false)
                navController.navigate(Screen.RequestLocationScreen.route){
                    popUpTo(Screen.SplashScreen.route) {
                        inclusive =true
                    }
                }
            } else {
                navController.navigate(ItemBottomNav.Home.route){
                    popUpTo(Screen.SplashScreen.route) {
                        inclusive =true
                    }
                }
            }
        }
//        }
    }

//    LaunchedEffect(key1 = true) {
//        scale.animateTo(
//            scaledegree,
//            animationSpec = tween(
//                durationMillis = 200,
//                easing = {
//                    OvershootInterpolator(2f).getInterpolation(it)
//                }
//            )
//        )
//        delay(500)
//        isLogoVisible = false
//        delay(2000)
//    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//        if (isLogoVisible) {
//            Image(
//                painter = painter,
//                contentDescription = "",
//                Modifier
//                    .clip(shape = RoundedCornerShape(50.dp))
//                    .scale(scale.value)
//                    .size((140.dp))
//            )
//        } else {
        if(isdbBuilt==false){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                LoadingAnimation(dotsColor = primaryColor)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "من فضلك انتظر قليلا", fontSize = 18.sp)
                Text(text = splashViewModel.getIsDbBuilt().toString(), fontSize = 18.sp)
            }
        }

//        }

    }
}


@Composable
fun LoadingAnimation(
    modifier: Modifier = Modifier,
    travelDistance: Dp = 20.dp,
    distanceBetween: Dp = 12.dp,
    dotsRaduis: Dp = 15.dp,
    dotsColor: Color
) {
    val circles = listOf( // animatable can animate float value via animateto
        remember { Animatable(0f) },
        remember { Animatable(0f) },
        remember { Animatable(0f) }
    )

    // how to animate each value of circle
    circles.forEachIndexed { index, animatable ->
        LaunchedEffect(key1 = animatable) {
            delay(index * 100L)
            animatable.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        durationMillis = 1200
                        0.0f at 0 with LinearOutSlowInEasing
                        1.0f at 300 with LinearOutSlowInEasing
                        0.0f at 600 with LinearOutSlowInEasing
                        0.0f at 1200 with LinearOutSlowInEasing
                    },
                    repeatMode = RepeatMode.Restart
                )
            )
        }
    }


    val circlesValues = circles.map { it.value }
    val distance = with(LocalDensity.current) { travelDistance.toPx() }

    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(distanceBetween)) {
        circlesValues.forEach { value ->
            Box(
                modifier = Modifier
                    .size(dotsRaduis)
                    .graphicsLayer { //used in scaling, rotation, opacity, shadow, and clipping
                        translationY = -value * distance
                    }
                    .background(
                        dotsColor,
                        shape = CircleShape
                    )

            )
        }
    }
}


