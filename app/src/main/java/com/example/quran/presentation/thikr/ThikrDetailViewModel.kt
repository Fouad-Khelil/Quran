package com.example.quran.presentation.thikr


import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quran.data.data_source.Entities.AdhkarEntity
import com.example.quran.data.repository.QuranAndThikrRepositoryImpl
import com.example.quran.others.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThikrDetailViewModel @Inject constructor(
    val quranRepository: QuranAndThikrRepositoryImpl
) : ViewModel() {

    private val _allThikrsByCategory = mutableStateOf<List<AdhkarEntity>>(emptyList())
    val allThikrByCategory: State<List<AdhkarEntity>> = _allThikrsByCategory


    fun getAllSurahs(category: String) {
        viewModelScope.launch {
            val categories = Constants.THIKR_REQUEST_LIST[category] ?: listOf("اخر")
            val isItRequestedList =  categories!= listOf("اخر")
            _allThikrsByCategory.value = quranRepository.getAllThikr(
                isItRequestedList,
                categories,
                Constants.THIKR_REQUEST_LIST.keys.toList()
            )

        }
    }

}