package com.example.quran.presentation.splash

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quran.data.repository.DataStoreRepository
import com.google.common.math.DoubleMath
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.wait
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    val dataStoreRepository: DataStoreRepository,
) : ViewModel() {

    fun saveLocation(
        latitude: Double, longtitude: Double,
        onSuccess: () -> Unit,
    ) {
        Log.d("saveLocation", "lat : $latitude , long :$longtitude")
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.setLatAndLng(latitude, longtitude)
            withContext(Dispatchers.Main){
                onSuccess()
            }
        }
    }

}