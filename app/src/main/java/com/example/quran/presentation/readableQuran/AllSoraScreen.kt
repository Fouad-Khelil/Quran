package com.example.quran.presentation.readableQuran

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quran.navigation.Screen
import com.example.quran.others.Constants
import com.example.quran.others.Resource
import com.example.quran.presentation.auditableQuran.viewmodels.AllSoraAudioViewModel
import com.example.quran.presentation.sharedUi.AllSoraScreen
import com.example.quran.presentation.sharedUi.SoraItm

@Composable
fun AllSoraTextScreen(
    navController: NavController,
    allSoraScreenViewModel: AllSoraScreenViewModel = hiltViewModel(),
) {
    val allSurah by allSoraScreenViewModel.allSurahs

    var lastReading by remember { mutableStateOf("") }

    allSoraScreenViewModel.getLastReadingSurah {
        lastReading = it
    }
    AllSoraScreen(
        sora = lastReading,
        hizbOrMokri = "سورة",
        surahs = allSurah,
        onClickLastReading = {
            navController.navigate(Screen.QuranPaperScreen.createRoute(Constants.NAVIGATE_FROM_LAST_READING))
        },
        onSearchSurah = allSoraScreenViewModel::getSearchedSurahs,
        onClickPlayIcon = {}
    ) { surahIndex ->
        navController.navigate(Screen.QuranPaperScreen.createRoute(surahIndex))
    }

}