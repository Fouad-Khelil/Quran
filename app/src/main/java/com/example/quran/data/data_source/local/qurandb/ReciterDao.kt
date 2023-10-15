package com.example.quran.data.data_source.local.qurandb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.quran.data.data_source.Entities.ReciterEntity


@Dao
interface ReciterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllReciters(reciters: List<ReciterEntity>)

    @Query("SELECT * from reciter")
    suspend fun getAllReciters(): List<ReciterEntity>

    @Query("SELECT * from reciter WHERE id = :reciterId")
    suspend fun getReciter(reciterId : Int): ReciterEntity


    @Query("SELECT * FROM reciter WHERE is_favorite = 1")
    fun getFavoriteReciters(): List<ReciterEntity>

    @Update
    suspend fun updateReciter(reciter: ReciterEntity)

}