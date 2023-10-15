package com.example.quran.presentation.auditableQuran

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quran.data.data_source.Entities.ReciterEntity
import com.example.quran.data.repository.DataStoreRepository
import com.example.quran.data.repository.ReciterRepositoryImpl
import com.example.quran.others.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RecitersViewModel @Inject constructor(
    val reciterRepository: ReciterRepositoryImpl,
    val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val _recitersList = MutableStateFlow<Resource<List<ReciterEntity>>>(Resource.Loading())
    val recitersList: MutableStateFlow<Resource<List<ReciterEntity>>> = _recitersList

    private var cachedReciterList = listOf<ReciterEntity>()


    init {
        getAllReciters(false)
    }



    fun getAllReciters(filterFavorite: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                cachedReciterList = if (filterFavorite) {
                    reciterRepository.getFavoriteReciters()
                } else {
                    reciterRepository.getAllReciters()
                }

                // Update the favorite status of reciters in the filtered list
                if (filterFavorite) {
                    val filteredReciters = cachedReciterList.filter { it.is_favorite }
                    _recitersList.value = Resource.Success(filteredReciters)
                } else {
                    _recitersList.value = Resource.Success(cachedReciterList)
                }
            } catch (e: Exception) {
                _recitersList.value = Resource.Error("something is wrong")
            }
        }
    }

    fun toggleFavorite(reciter: ReciterEntity, filterFavorite: Boolean) {
        val targetReciter = _recitersList.value.data?.find { it.id == reciter.id }

        if (targetReciter != null) {
            targetReciter.is_favorite = !targetReciter.is_favorite

            if (filterFavorite) {
                // If we're in the "favorite" filter mode and the reciter is unfavorited,
                // remove it from the filtered list
                if (!targetReciter.is_favorite) {
                    cachedReciterList = cachedReciterList.filter { it != targetReciter }
                }
                // Update the state flow with the new filtered list
                _recitersList.value = Resource.Success(cachedReciterList.toList())
            }else{
                _recitersList.value = Resource.Success(cachedReciterList.toList()) // Make sure to create a new list
            }

            viewModelScope.launch(Dispatchers.IO) {
                reciterRepository.markAsFavorite(reciter)
            }
        }
    }



    fun searchReciter(searchedReciter : String) {
        Log.d("reciter", "searchReciter: $searchedReciter")
//        viewModelScope.launch {
            if(searchedReciter.isBlank()){
                 _recitersList.value = Resource.Success(cachedReciterList)
            }else {
                _recitersList.value = Resource.Success(cachedReciterList.filter { reciter ->
                    reciter.name.contains(searchedReciter)
                })
            }
//        }
    }


    fun saveLastReciter(reciter : ReciterEntity)  {
        dataStoreRepository.saveLastReciter(reciter)
    }
    
}
