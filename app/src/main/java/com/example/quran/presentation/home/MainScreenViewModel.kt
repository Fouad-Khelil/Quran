package com.example.quran.presentation.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batoulapps.adhan.CalculationMethod
import com.batoulapps.adhan.Coordinates
import com.batoulapps.adhan.PrayerTimes
import com.batoulapps.adhan.data.DateComponents
import com.example.quran.data.data_source.Entities.AdhkarEntity
import com.example.quran.data.repository.DataStoreRepository
import com.example.quran.data.repository.QuranAndThikrRepositoryImpl
import com.example.quran.others.AyahPrettyTextTransformer
import com.example.quran.others.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.random.Random

@Suppress("DEPRECATION")
@HiltViewModel
class MainScreenViewModel @Inject constructor(
    val quranRepository: QuranAndThikrRepositoryImpl,
    val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val _randomAyah = mutableStateOf(RandomAyah())
    val randomAyah: State<RandomAyah> = _randomAyah

    private val _randomThikr = mutableStateOf(AdhkarEntity(null, "", "", "", "", ""))
    val randomThikr: State<AdhkarEntity> = _randomThikr


    private val _randomHadith = mutableStateOf("")
    val randomHadith: State<String> = _randomHadith


    fun getRandom_Ayah_Thikr() {
        viewModelScope.launch {
            val randomNumber = Random.nextInt(1, Constants.AYAY_NUMBER)
            quranRepository.getAyahByIndex(randomNumber).collect {
                val surah = withContext(Dispatchers.Default) {
                    quranRepository.getSurahName(it.surahIndex)
                }
                val ayah = AyahPrettyTextTransformer.removeUnsupportedChars(it.text)
                _randomAyah.value = RandomAyah(
                    surah = surah,
                    ayah = ayah,
                    numberInSurah = it.numberInSurah
                )
            }

        }
        viewModelScope.launch {
            val randomNumber = Random.nextInt(1, Constants.THIKR_NUMBER)
            quranRepository.getThikrByIndex(randomNumber).collect {
                _randomThikr.value = it
            }
        }


    }


    fun getRandomHadith() {
        val randomNumber = Random.nextInt(0, Constants.NAWAWI_HADITH.size)
        _randomHadith.value = Constants.NAWAWI_HADITH[randomNumber]
    }

    fun getProgress(): Int {
        var progress = 0
        viewModelScope.launch {
            val page = dataStoreRepository.getLastReadingPage()
            progress = page * 100 / Constants.QURAN_NUMBRE_OF_PAGES
        }
        return progress
    }


    fun getLocation() = dataStoreRepository.getLatAndLng()

    @SuppressLint("SimpleDateFormat")
    fun getPrayerTimes(calculationMethod: String): List<String> {
//        viewModelScope.launch {
        val location = getLocation()

        val coordinates = Coordinates(location.lat, location.lng)
        val dateComponent = DateComponents.from(Date())
        val params = getCalculationMethod(calculationMethod)
        val prayerTimes = PrayerTimes(coordinates, dateComponent, params)

        val formatter = SimpleDateFormat("hh:mm")
        formatter.timeZone = TimeZone.getDefault()
        val fajr = formatter.format(prayerTimes.fajr)
        val dhuhr = formatter.format(prayerTimes.dhuhr)
        val asr = formatter.format(prayerTimes.asr)
        val maghrib = formatter.format(prayerTimes.maghrib)
        val isha = formatter.format(prayerTimes.isha)
        val nextPrayer = prayerTimes.nextPrayer()

        return listOf(fajr, dhuhr, asr, maghrib, isha, nextPrayer.ordinal.toString())
//        }
    }

    fun getLastReciter() = dataStoreRepository.getLastReciter()

    fun getLastReadingSurah(onComplete: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val page = dataStoreRepository.getLastReadingPage()
            val surah = quranRepository.getSurahByPage(page) ?: "البقرة"
            withContext(Dispatchers.Main) {
                onComplete(surah) // Pass the result to the callback on the main thread
            }
        }
    }


    fun init() {
        getRandom_Ayah_Thikr()
        getRandomHadith()
    }
}


fun getCalculationMethod(calculationMethod: String) = when (calculationMethod) {
    Constants.ISLAMIC_LEAGUE_METHOD -> CalculationMethod.MUSLIM_WORLD_LEAGUE.parameters
    Constants.EGYPTIAN_METHOD -> CalculationMethod.EGYPTIAN.parameters
    Constants.KARACHI_METHOD -> CalculationMethod.KARACHI.parameters
    Constants.UMM_AL_QURA_METHOD -> CalculationMethod.UMM_AL_QURA.parameters
    Constants.DUBAI_METHOD -> CalculationMethod.DUBAI.parameters
    Constants.MOON_SIGHTING_COMMITTEE_METHOD -> CalculationMethod.MOON_SIGHTING_COMMITTEE.parameters
    Constants.NORTH_AMERICA_METHOD -> CalculationMethod.NORTH_AMERICA.parameters
    Constants.KUWAIT_METHOD -> CalculationMethod.KUWAIT.parameters
    Constants.QATAR_METHOD -> CalculationMethod.QATAR.parameters
    Constants.SINGAPORE_METHOD -> CalculationMethod.SINGAPORE.parameters
    else -> {
        CalculationMethod.OTHER.parameters
    }
}


data class RandomAyah(val ayah: String = "", val surah: String = "", val numberInSurah: Int = 0)