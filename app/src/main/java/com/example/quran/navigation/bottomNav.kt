package com.example.quran.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.quran.R
import com.example.quran.others.isItInDarkTheme
import com.example.quran.presentation.auditableQuran.AllSoraAudioScreen
import com.example.quran.presentation.auditableQuran.AudioPlayerScreen
import com.example.quran.presentation.auditableQuran.RecitersScreen
import com.example.quran.presentation.auditableQuran.dual_mode.SoraAudioDualMode
import com.example.quran.presentation.home.MainScreen
import com.example.quran.presentation.otherApps.AboutTheApp.AboutTheAppScreen
import com.example.quran.presentation.otherApps.HadithScreen
import com.example.quran.presentation.otherApps.OtherAppsScreen
import com.example.quran.presentation.otherApps.names_of_allah.NamesOfAllahScreen
import com.example.quran.presentation.otherApps.qibla.QiblaScreen
import com.example.quran.presentation.otherApps.settings.SettingScreen
import com.example.quran.presentation.readableQuran.AllSoraTextScreen
import com.example.quran.presentation.readableQuran.QuranPaperWithBottomCheetScreen
import com.example.quran.presentation.readableQuran.search.AyahSearchScreen
import com.example.quran.presentation.splash.LocationRequestScreen
import com.example.quran.presentation.splash.SplashScreen
import com.example.quran.presentation.thikr.ThikrDetailScreen
import com.example.quran.presentation.thikr.ThirkrScreen


@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    items: List<ItemBottomNav>,
    navController: NavController,
    onItemClick: (ItemBottomNav) -> Unit
) {

    val backStackEntry = navController.currentBackStackEntryAsState()
    BottomNavigation(
        modifier = modifier,
        backgroundColor = if (isItInDarkTheme()) Color(0xFF282B28) else Color.White,
        elevation = 5.dp
    ) {

        items.forEach {
            val isSelected = it.route == backStackEntry.value?.destination?.route
            BottomNavigationItem(
                selected = isSelected,
                selectedContentColor = MaterialTheme.colors.primary,
                unselectedContentColor = if (isItInDarkTheme()) Color(0xFF686868) else Color.LightGray,
                onClick = {
                    onItemClick(it)
                },
                icon = {
                    Icon(
                        painter = painterResource(id = it.icon),
                        contentDescription = "",
                        tint = if (isSelected) MaterialTheme.colors.primary else {
                            if (isItInDarkTheme()) Color(0xFF686868) else Color.LightGray
                        }
                    )
                }
            )

        }

    }
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun NavigationGraph(
    navController: NavHostController,
    onThemeUpdated: (Int) -> Unit
) {


    NavHost(
        navController = navController,
        startDestination = Screen.SplashScreen.route,
        modifier = Modifier.semantics {
            testTagsAsResourceId = true
        }
    ) {
        composable(Screen.SplashScreen.route) {
            SplashScreen(
                navController = navController,
                scaledegree = 1f,
                painter = painterResource(id = R.drawable.img_logo_v),
            )
        }

        composable(ItemBottomNav.Home.route) {
            MainScreen(navController)
        }

        composable(ItemBottomNav.AuditableQuran.route) {
            RecitersScreen(navController)
        }

        composable(ItemBottomNav.ReadableQuran.route) {
            AllSoraTextScreen(navController)
        }

        composable(ItemBottomNav.Doa.route) {
            ThirkrScreen(navController)
        }


        composable(
            route = Screen.ThikrDetailScreen.route,
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry: NavBackStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: "اخرى"
            ThikrDetailScreen(category)
        }

        composable(
            Screen.QuranPaperScreen.route,
            arguments = listOf(
                navArgument("surah_index") {
                    type = NavType.IntType
                    defaultValue = 1
                },
                navArgument("page") {
                    type = NavType.IntType
                    defaultValue = 1
                },
            )
        ) { backStackEntry: NavBackStackEntry ->
            val surahIndex = backStackEntry.arguments?.getInt("surah_index") ?: 1
            val page = backStackEntry.arguments?.getInt("page") ?: 1
            QuranPaperWithBottomCheetScreen(
                surahIndex,
                page,
                navController,
                onThemeUpdated
            )
        }

        composable(
            Screen.AllSoraAudioScreen.route,
            arguments = listOf(
                navArgument("reciter_name") { type = NavType.StringType },
                navArgument("rewayah") { type = NavType.StringType },
                navArgument("photo") { type = NavType.StringType },
                navArgument("server") { type = NavType.StringType },
                navArgument("surah_list") { type = NavType.StringType },
            )
        ) { backStackEntry: NavBackStackEntry ->
            val reciterName = backStackEntry.arguments?.getString("reciter_name") ?: ""
            val rewayah = backStackEntry.arguments?.getString("rewayah") ?: ""
            val photo = backStackEntry.arguments?.getString("photo") ?: ""
            val server = backStackEntry.arguments?.getString("server") ?: ""
            val surahList = backStackEntry.arguments?.getString("surah_list") ?: ""

            AllSoraAudioScreen(
                navController = navController,
                reciterName = reciterName,
                rewayah = rewayah,
                photo = photo,
                server = server,
                surahList = surahList,
            )
        }

        composable(
            Screen.AudioPlayerScreen.route,
            arguments = listOf(navArgument("rewayah") { type = NavType.StringType })
        ) { backStackEntry: NavBackStackEntry ->
            val rewayah = backStackEntry.arguments?.getString("rewayah") ?: ""
            AudioPlayerScreen(rewayah, navController)
        }


        composable(
            Screen.AudioDualScreen.route,
            arguments = listOf(navArgument("reciter_index") { type = NavType.IntType })
        ) { backStackEntry: NavBackStackEntry ->
            val index = backStackEntry.arguments?.getInt("reciter_index") ?: 0
            SoraAudioDualMode(navController, index)
        }

        composable(Screen.RequestLocationScreen.route) {
            LocationRequestScreen(onNavigate = {
                navController.navigate(ItemBottomNav.Home.route) {
                    popUpTo(Screen.RequestLocationScreen.route) {
                        inclusive = true
                    }
                }
            })
        }

        composable(ItemBottomNav.Others.route) {
//            LocationScreen()
            OtherAppsScreen(navController)
        }

        composable(Screen.QiblaCompassScreen.route) {
//            LocationScreen()
            QiblaScreen(navController)
        }

        composable(Screen.AyahSearchScreen.route) {
            AyahSearchScreen(navController)
        }

        composable(Screen.AboutTheAppScreen.route) {
            AboutTheAppScreen(navController)
        }
        composable(Screen.SettingsScreen.route) {
            SettingScreen(navController, onThemeUpdated)
        }
        composable(Screen.NamesOfAllahScreen.route) {
            NamesOfAllahScreen(navController)
        }
        composable(Screen.HadithScreen.route) {
            HadithScreen(navController)
        }

        composable(Screen.LocationSettingScreen.route) {
            LocationRequestScreen(onNavigate = {
                navController.navigate(Screen.SettingsScreen.route) {
                    popUpTo(Screen.LocationSettingScreen.route) {
                        inclusive = true
                    }
                }
            })
        }

    }
}

//                    AllSoraScreen()
//                    ThirkrScreen()
//                    ThikrDetailScreen()
