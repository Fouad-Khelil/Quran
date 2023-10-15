package com.example.quran.presentation.readableQuran

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quran.data.data_source.Entities.AyahEntity
import com.example.quran.data.repository.DataStoreRepository
import com.example.quran.data.repository.QuranAndThikrRepositoryImpl
import com.example.quran.others.AyahPrettyTextTransformer
import com.example.quran.others.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class QuranPaperViewModel @Inject constructor(
    val quranRepo: QuranAndThikrRepositoryImpl,
    val dataStoreRepository: DataStoreRepository
    ) : ViewModel() {

    private val _pageAyahs = mutableStateOf("")
    val pageAyahs: State<String> = _pageAyahs

    private val _currentPage = MutableStateFlow(0)
    val currentPage: MutableStateFlow<Int> = _currentPage

    private val _currentSurah = MutableStateFlow("")
    val currentSurah: MutableStateFlow<String> = _currentSurah

    private val _tafsirSurahName = MutableStateFlow("")
    val tafsirSurahName: MutableStateFlow<String> = _tafsirSurahName

    private val _isCurrentPageReady = mutableStateOf(false)
    val isCurrentPageReady : State<Boolean> = _isCurrentPageReady

    private val _quranPageTafseerList = mutableStateOf<List<AyahEntity>>(listOf())
    val quranPageTafseerList : State<List<AyahEntity>> = _quranPageTafseerList





    fun setCurrentPage(page:Int) {
        _currentPage.value= page
    }

    fun getSurahByPage(page: Int){
        viewModelScope.launch(Dispatchers.IO) {
            _currentSurah.value =quranRepo.getSurahByPage(page)?:""
        }
    }

    fun getSurahName(index: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _tafsirSurahName.value =quranRepo.getSurahName(index)
        }
    }

    fun getSurahPage(surahIndex: Int) {
            viewModelScope.launch(Dispatchers.IO) {
            val quranPage = quranRepo.getSurahFirstPage(surahIndex)
            val sbPage = StringBuilder()
            if (quranPage.isNotEmpty()){
                _currentPage.value = quranPage[0].page
            }
            for (ayah in quranPage) {
                ayah.text = AyahPrettyTextTransformer.removeUnsupportedChars(ayah.text)
                sbPage.append(AyahPrettyTextTransformer.appendAyahEndDecorator(ayah))
            }
            _pageAyahs.value = sbPage.toString()
            _quranPageTafseerList.value = quranPage
            _isCurrentPageReady.value =true
            Log.d("paper", "index 1: ${surahIndex}")
            Log.d("paper", "index 2: ${_currentPage.value}")

        }

    }


    fun getQuranPage(page: Int = Constants.DEFAULT_QURAN_PAGE) {
        viewModelScope.launch(Dispatchers.IO) {
            val sbPage = StringBuilder()
            val quranPage = quranRepo.getPageByPageNumber(page)
            if (quranPage.isNotEmpty()){
                _currentPage.value = quranPage[0].page
            }
            Log.d("paper", "number : ${quranPage.size}")
            for (ayah in quranPage) {
                ayah.text = AyahPrettyTextTransformer.removeUnsupportedChars(ayah.text)
                sbPage.append(AyahPrettyTextTransformer.appendAyahEndDecorator(ayah))
            }
            _pageAyahs.value = sbPage.toString()
            _quranPageTafseerList.value = quranPage
            _isCurrentPageReady.value =true
            Log.d("paper", "number : ${_pageAyahs.value.isNotEmpty()}")
        }
    }

    fun saveLastReadingPage(page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.setLastReadingPage(page)
        }
    }

    fun getLastReadingPage() {
        viewModelScope.launch(Dispatchers.IO) {
            _currentPage.value =dataStoreRepository.getLastReadingPage()
            getQuranPage(_currentPage.value)
        }
    }


    fun getPageOfAyah(page: Int){
        viewModelScope.launch(Dispatchers.IO) {
            _currentPage.value =page
            getQuranPage(_currentPage.value)
        }
    }

//    fun getSurahByIndex (surahIndex: Int)  {
//         quranRepo.getSurahName(surahIndex)
//    }
    
}