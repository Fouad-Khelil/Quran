package com.example.quran

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection.Rtl
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.quran.data.repository.DataStoreRepository
import com.example.quran.navigation.BottomNavigationBar
import com.example.quran.navigation.ItemBottomNav
import com.example.quran.navigation.NavigationGraph
import com.example.quran.navigation.Screen
import com.example.quran.others.Constants
import com.example.quran.others.isNotInHidenScreen
import com.example.quran.presentation.splash.ConnectionState
import com.example.quran.presentation.splash.connectivityState
import com.example.quran.ui.theme.QuranTheme
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@Suppress("DEPRECATION")
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition {
            false
        }
//        ActivityCompat.requestPermissions(
//            this,
//            arrayOf(
//                ACCESS_COARSE_LOCATION,
//                ACCESS_FINE_LOCATION,
//            ),
//            0
//        )


        setContent {

            var theme by remember { mutableStateOf(DataStoreRepository(this).getTheme()) }
            val isDarktheme = when (theme) {
                Constants.LIGHT_THEME -> false
                Constants.DARK_THEME -> true
                else -> {
                    isSystemInDarkTheme()
                }
            }
            val context = LocalContext.current

            QuranTheme(darkTheme = isDarktheme) {
                CompositionLocalProvider(LocalLayoutDirection provides Rtl) {

                    val navController = rememberNavController()

                    val backStackEntry = navController.currentBackStackEntryAsState()
                    val isNotHidden = backStackEntry.value?.destination?.route?.let {
                        isNotInHidenScreen(
                            it
                        )
                    }

                    Scaffold(
                        bottomBar = {
                            if (isNotHidden != false) {
                                BottomNavigationBar(
                                    items = listOf(
                                        ItemBottomNav.Home,
                                        ItemBottomNav.AuditableQuran,
                                        ItemBottomNav.ReadableQuran,
                                        ItemBottomNav.Doa,
                                        ItemBottomNav.Others,
                                    ),
                                    navController = navController,
                                    onItemClick = { item ->
                                        navController.navigate(item.route) {
                                            popUpTo(navController.graph.findStartDestination().id)
                                            launchSingleTop =
                                                true// this to avoid multiple coppies of the same destination when reselect it
                                        }
                                    }
                                )
                            }

                        }) {
                        NavigationGraph(
                            navController,
                            isDarktheme,
                            onThemeUpdated = { newTheme ->
                                theme = newTheme
                                DataStoreRepository(context).setTheme(theme)
                            }

                        )
//                        LocationRequestScreen(navController)
                    }
                }
            }
        }
    }


}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    QuranTheme {
    }
}

