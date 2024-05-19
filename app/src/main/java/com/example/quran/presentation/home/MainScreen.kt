@file:Suppress("DEPRECATION")

package com.example.quran.presentation.home

import CardMain
import PrayerTimesRow
import TodayItem
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quran.R
import com.example.quran.data.repository.DataStoreRepository
import com.example.quran.exoplayer.QuranService
import com.example.quran.navigation.Screen
import com.example.quran.others.Constants
import com.example.quran.ui.theme.noto_arabic_font
import khatmaProgressIndicator
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter
import java.util.*


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(
    navController: NavController,
    mainScreenViewModel: MainScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val randomAyah by mainScreenViewModel.randomAyah
    val randomThikr by mainScreenViewModel.randomThikr
    val randomHadith by mainScreenViewModel.randomHadith

    val datastore = DataStoreRepository(context)

    val prayertimes = mainScreenViewModel.getPrayerTimes(datastore.getCalculationMethod())
    val nextPTIndex = prayertimes[5].toInt()
    Log.d("prayertimes", "MainScreen: $nextPTIndex")
    val nextPrayerTime = if (nextPTIndex <= 1) 1 else if (nextPTIndex == 2) 2 else nextPTIndex - 1

    val reciter = mainScreenViewModel.getLastReciter()
    val intent = Intent(context, QuranService::class.java).apply {
        putExtra("reciter", reciter.id)
    }
    var lastReadingSurah by remember{ mutableStateOf("") }

    LaunchedEffect(key1 = true){
        mainScreenViewModel.init()
        mainScreenViewModel.getLastReadingSurah{surah->
            lastReadingSurah=surah
        }
    }

    Column(
        modifier = Modifier.verticalScroll(state = rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        val currentDate = getCurrentDate()

        val islamicDate = getCurrentIslamicDate()
        CardMain(
            nextSalat = prayerTimeMap[nextPrayerTime] ?: "الظهر",
            salatTime = prayertimes[nextPrayerTime - 1],
            date = currentDate,
            hijriDate = islamicDate
        )
        PrayerTimesRow(prayertimes, nextPrayerTime)
        DecorationMain(
            lastReadingSurah =lastReadingSurah ,
            lastListeningSurah =datastore.getLastListeningSurah() ,
            lastReciter = reciter.name,
            onClickLastReading = {
                navController.navigate(Screen.QuranPaperScreen.createRoute(Constants.NAVIGATE_FROM_LAST_READING))
            }, onClickLastListening = {
                context.startService(intent)
                val encodedUrlPhoto =
                    URLEncoder.encode(reciter.photo, StandardCharsets.UTF_8.toString())
                val encodedUrlServer =
                    URLEncoder.encode(reciter.server, StandardCharsets.UTF_8.toString())
                navController.navigate(
                    Screen.AllSoraAudioScreen.createRoute(
                        reciter.name,
                        reciter.rewayah,
                        encodedUrlPhoto,
                        encodedUrlServer,
                        reciter.surah_list
                    )
                )
            }
        )

        khatmaProgressIndicator(mainScreenViewModel.getProgress())
        TodayItem(R.drawable.ic_aya, "اية", randomAyah.surah, randomAyah.ayah)
        TodayItem(
            R.drawable.ic_book_, "حديث", "الأربعون النووية",
            randomHadith,
            fontFamily = noto_arabic_font,
            fontSize = 14.sp
        )

        val extended_text = if (randomThikr.count == "1") "مرة" else if (randomThikr.count =="2") "مرتان" else if (randomThikr.count > "2") "${randomThikr.count}" + "مرات" else ""
        TodayItem(
            R.drawable.dua_hands, "ذكر", " ${randomThikr.reference} - "+ extended_text,
            content = randomThikr.zekr,
//            "اللّهُـمَّ إِنِّـي أَصْبَـحْتُ أُشْـهِدُك , وَأُشْـهِدُ حَمَلَـةَ عَـرْشِـك , وَمَلَائِكَتَكَ , وَجَمـيعَ خَلْـقِك , أَنَّـكَ أَنْـتَ اللهُ لا إلهَ إلاّ أَنْـتَ وَحْـدَكَ لا شَريكَ لَـك , وَأَنَّ ُ مُحَمّـداً عَبْـدُكَ وَرَسـولُـك",
            fontFamily = noto_arabic_font,
            fontSize = 14.sp
        )
        Spacer(Modifier.height(56.dp))
    }
}


@SuppressLint("SimpleDateFormat")
fun getCurrentDate(): String {

    val date = Date()
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    return formatter.format(date)
}

@RequiresApi(Build.VERSION_CODES.O)
fun getCurrentIslamicDate() :String{
    val date = getCurrentDate().split("-")

    val year = date[0].toInt()
    val month =  date[1].toInt()
    val day = date[2].toInt()

    Log.d("date_tag", "---year:${date[0].toInt()} month:${ date[1].toInt()} day : ${date[2].toInt()} ")

    val dt = LocalDate.of(year, month, day)
    val hijrahDate = HijrahDate.from(dt)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return formatter.format(hijrahDate)
}

val prayerTimeMap = mapOf(
    1 to "الفجر",
    2 to "الظهر",
    3 to "العصر",
    4 to "المغرب",
    5 to "العشاء",
)