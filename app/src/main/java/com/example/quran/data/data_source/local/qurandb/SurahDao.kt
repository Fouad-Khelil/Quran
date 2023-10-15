package com.example.quran.data.data_source.local.qurandb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quran.data.data_source.Entities.AyahEntity
import com.example.quran.data.data_source.Entities.SurahEntity


@Dao
interface SurahDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSurahs(surahs: List<SurahEntity>)

    @Query("SELECT * from surahs")
    suspend fun getAllSurahs() : List<SurahEntity>

    @Query("SELECT name FROM surahs WHERE number = :index")
    fun getSurahName(index :Int) : String

    @Query("SELECT name from surahs")
    suspend fun getAllSurahsNames() : List<String>





//    @Query("SELECT * from surahs WHERE name LIKE :surah order by number asc")
//    suspend fun getSearchedSurah(surah : String) : List<SurahEntity>
}