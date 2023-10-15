package com.example.quran.data.repository

import com.example.quran.data.data_source.Entities.AdhkarEntity
import com.example.quran.data.data_source.Entities.AyahEntity
import com.example.quran.data.data_source.Entities.AyahForSearchEntity
import com.example.quran.data.data_source.Entities.SurahEntity
import kotlinx.coroutines.flow.Flow

interface QuranAndThikrRepository {

    suspend fun addAllAyahs(ayahs: List<AyahEntity>)
    fun getPageOfAyahIndex(ayahNumber: Int): Flow<List<AyahEntity>>
    fun getPageByPageNumber(page: Int): List<AyahEntity>
    fun getSearchedAyah(searchText: String): Flow<List<AyahEntity>>
    fun getAyahByIndex(index: Int): Flow<AyahEntity>
    fun getSurahFirstPage(surahIndex: Int): List<AyahEntity>
    fun getAyahTafseer(index: Int): Flow<List<AyahEntity>>

    suspend fun addSurahs(surahs: List<SurahEntity>)
    suspend fun getAllSurahs(): List<SurahEntity>

    fun getThikrByIndex(index: Int): Flow<AdhkarEntity>

    suspend fun getAllThikr(
        isItInResquestedList: Boolean,
        requestedList: List<String>,
        excludedList: List<String>
    ): List<AdhkarEntity>

    suspend fun addAdhkars(adhkars: List<AdhkarEntity>)

    fun getSurahName(index: Int): String
    suspend fun addAllForSeachAyahs(ayahs: List<AyahForSearchEntity>)
    suspend fun searchAyahByText(searchText: String): List<AyahForSearchEntity>
    fun getPageBySurahNumAndAyahNum(surahIndex : Int , ayahIndex :Int) : Int
    fun getSurahByPage(page : Int) : String?
    fun getAyahBySurahNumAndAyahNum(surahIndex : Int , ayahIndex :Int) : String





}