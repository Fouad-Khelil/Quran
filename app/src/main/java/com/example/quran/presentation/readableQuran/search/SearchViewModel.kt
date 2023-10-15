package com.example.quran.presentation.readableQuran.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quran.data.data_source.Entities.AyahForSearchEntity
import com.example.quran.data.repository.QuranAndThikrRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    val quranRepository: QuranAndThikrRepositoryImpl,
) : ViewModel() {

    private val _ayahsSearchResult = MutableStateFlow<List<AyahForSearchEntity>>(listOf())
    val ayahsSearchResult: MutableStateFlow<List<AyahForSearchEntity>> = _ayahsSearchResult

    private val _isSearchPerformed = MutableStateFlow(false)
    val isSearchPerformed: MutableStateFlow<Boolean> = _isSearchPerformed

    fun search(text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _ayahsSearchResult.value = quranRepository.searchAyahByText(text)
                _isSearchPerformed.value = true
            } catch (e: Exception) {
                Log.d("search_view_model", "getAllReciters: ")
            }

        }
    }


    fun getPageFromSurahNumAndAyahNum(
        surahNum: Int,
        ayahNum: Int,
        onComplete: (Int) -> Unit // Callback to handle the result
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val page = quranRepository.getPageBySurahNumAndAyahNum(surahNum, ayahNum)
            withContext(Dispatchers.Main) {
                onComplete(page) // Pass the result to the callback on the main thread
            }
        }
    }

}