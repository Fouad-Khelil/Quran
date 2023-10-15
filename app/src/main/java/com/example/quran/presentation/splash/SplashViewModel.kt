package com.example.quran.presentation.splash

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quran.data.data_source.local.qurandb.DbBuilder
import com.example.quran.data.repository.DataStoreRepository
import com.example.quran.data.repository.NamesOfAllahRepositoryImpl
import com.example.quran.data.repository.QuranAndThikrRepositoryImpl
import com.example.quran.data.repository.ReciterRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    val dataStoreRepository: DataStoreRepository,
    val quranRepository: QuranAndThikrRepositoryImpl,
    val recitersRepository: ReciterRepositoryImpl,
    val namesOfAllahRepository: NamesOfAllahRepositoryImpl
) : ViewModel() {

    private val _isDbBuilt: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val isdbBuilt: MutableStateFlow<Boolean?> = _isDbBuilt


    init {
        getIsDbBuilt()
    }



    fun loadDb(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            DbBuilder(
                quranRepository,
                context = context,
                recitersRepository = recitersRepository ,
                namesOfAllahRepository = namesOfAllahRepository
            ).build()
            Log.d("datastore", "before: ")
            saveOnLoadQuranState(true)
            _isDbBuilt.value = true
            Log.d("datastore", "after: ")
        }
    }

    fun saveOnLoadQuranState(completed: Boolean) {
        dataStoreRepository.saveIsDbBuilt(completed)
    }

    fun getIsDbBuilt() = dataStoreRepository.getIsDbBuilt()

    fun setIsFirstTime(firstTime: Boolean) {
        dataStoreRepository.setIsFirstTime(firstTime)
    }
    fun getIsFirstTime() = dataStoreRepository.getIsFirstTime()

}
