package com.example.quran.presentation.readableQuran

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quran.data.data_source.Entities.SurahEntity
import com.example.quran.data.repository.DataStoreRepository
import com.example.quran.data.repository.QuranAndThikrRepositoryImpl
import com.example.quran.presentation.sharedUi.SoraItm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AllSoraScreenViewModel @Inject constructor(
    val quranRepository: QuranAndThikrRepositoryImpl ,
    val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val _allSurahs = mutableStateOf<List<SoraItm>>(listOf())
    val allSurahs: State<List<SoraItm>> = _allSurahs

    private var cachedListSurah = listOf<SoraItm>()

    fun getAllSurahs() {
        viewModelScope.launch {
            val quranSurahs = quranRepository.getAllSurahs()
            _allSurahs.value = mapSurahToSora(quranSurahs)
            cachedListSurah = _allSurahs.value
        }
    }

    fun getSearchedSurahs(surah: String) {
        if (surah.isBlank()) {
            _allSurahs.value = cachedListSurah
        }
        _allSurahs.value = _allSurahs.value.filter { surahQuran ->
            surahQuran.soraName.contains(surah)
        }
    }

    fun getReciterAvailableSurahs(surahsList: String) {
        val sList =  surahsList.split(",").map{
            it.toInt()
        }
        _allSurahs.value = _allSurahs.value.filter { surahQuran ->
            surahQuran.soraOrder in sList
        }
    }

    fun mapSurahToSora(surahList: List<SurahEntity>): List<SoraItm> {
        return surahList.map { surah ->
            SoraItm(
                soraOrder = surah.number,
                soraName = surah.name.substring(5),
                makiOrMadani = if (surah.revelationType == "Meccan") "مكية" else "مدنية",
                numberOfAya = surah.numberOfAyah,
                lastReading = "" //todo change last reading in db
            )
        }
    }

    fun getLastListeningSurah() = dataStoreRepository.getLastListeningSurah()

    fun getLastReadingSurah(onComplete: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val page = dataStoreRepository.getLastReadingPage()
            val surah= quranRepository.getSurahByPage(page) ?: "البقرة"
            withContext(Dispatchers.Main) {
                onComplete(surah) // Pass the result to the callback on the main thread
            }
        }
    }

    init {
        viewModelScope.launch (Dispatchers.IO){
            getAllSurahs()
        }
    }

}