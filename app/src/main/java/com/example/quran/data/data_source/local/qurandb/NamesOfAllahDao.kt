package com.example.quran.data.data_source.local.qurandb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quran.data.data_source.Entities.NameOfAllahEntity


@Dao
interface NamesOfAllahDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllNames(namesOfAllah: List<NameOfAllahEntity>)

    @Query("SELECT * from names_of_allah")
    suspend fun getAllNames(): List<NameOfAllahEntity>
}