package com.example.quran.presentation.otherApps.names_of_allah

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quran.data.data_source.Entities.NameOfAllahEntity
import com.example.quran.data.repository.NamesOfAllahRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NamesOfAllahViewModel @Inject constructor(
    val namesOfAllahRepository: NamesOfAllahRepositoryImpl
) : ViewModel() {

    private val _allNames = mutableStateOf<List<NameOfAllahEntity>>(listOf())
    val allNames: State<List<NameOfAllahEntity>> = _allNames

    fun getAllNamesofAllah() {
        viewModelScope.launch {
            Log.d("getAllNamesofAllah", "getAllNamesofAllah: "+namesOfAllahRepository.getAllNames())
            _allNames.value = namesOfAllahRepository.getAllNames()
        }
    }
}