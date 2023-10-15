package com.example.quran.data.repository

import com.example.quran.data.data_source.Entities.ReciterEntity
import kotlinx.coroutines.flow.Flow

interface ReciterRepository {
    suspend fun addAllReciters(reciters: List<ReciterEntity>)
    suspend fun getAllReciters(): List<ReciterEntity>
    suspend fun getFavoriteReciters(): List<ReciterEntity>
    suspend fun markAsFavorite(reciter: ReciterEntity)
}