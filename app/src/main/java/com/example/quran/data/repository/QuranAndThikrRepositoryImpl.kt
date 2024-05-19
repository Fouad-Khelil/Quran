package com.example.quran.data.repository

import com.example.quran.data.data_source.Entities.AdhkarEntity
import com.example.quran.data.data_source.Entities.AyahEntity
import com.example.quran.data.data_source.Entities.AyahForSearchEntity
import com.example.quran.data.data_source.Entities.SurahEntity
import com.example.quran.data.data_source.local.qurandb.QuranDb
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class QuranAndThikrRepositoryImpl @Inject constructor(
    val quranDb: QuranDb
) : QuranAndThikrRepository {

    override suspend fun addAllAyahs(ayahs: List<AyahEntity>) {
        quranDb.ayahDao.addAllAyahs(ayahs)
    }

    override suspend fun addAllForSeachAyahs(ayahs: List<AyahForSearchEntity>) {
        quranDb.ayahDao.addAllForSearchAyahs(ayahs)
    }

    override fun getPageOfAyahIndex(ayahNumber: Int): Flow<List<AyahEntity>> {
        return quranDb.ayahDao.getPageOfAyahIndex(ayahNumber)
    }

    override fun getPageByPageNumber(page: Int): List<AyahEntity> {
        return quranDb.ayahDao.getPageByPageNumber(page)
    }

    override fun getSearchedAyah(searchText: String): Flow<List<AyahEntity>> {
        return quranDb.ayahDao.getSearchedAyah(searchText)
    }

    override fun getAyahByIndex(index: Int): Flow<AyahEntity> {
        return quranDb.ayahDao.getAyahByIndex(index)
    }

    override fun getSurahFirstPage(surahIndex: Int): List<AyahEntity> {
        return quranDb.ayahDao.getSurahFirstPage(surahIndex)
    }

    override fun getAyahTafseer(index: Int): Flow<List<AyahEntity>> {
        return quranDb.ayahDao.getAyahTafseer(index)
    }

    override suspend fun addSurahs(surahs: List<SurahEntity>) {
        quranDb.surahDao.addSurahs(surahs)
    }


    override fun getSurahName(index: Int): String {
        return quranDb.surahDao.getSurahName(index)
    }


    override suspend fun getAllSurahs(): List<SurahEntity> {
        return quranDb.surahDao.getAllSurahs()
    }

    override suspend fun getAllThikr(
        isItInResquestedList: Boolean,
        requestedList: List<String>,
        excludedList: List<String>
    ):List<AdhkarEntity> {
        return quranDb.thikrDao.getAllThikr(isItInResquestedList , requestedList , excludedList)
    }

    override fun getThikrByIndex(index: Int): Flow<AdhkarEntity> {
        return quranDb.thikrDao.getThikrByIndex(index)
    }

    override suspend fun addAdhkars(adhkars: List<AdhkarEntity>) {
        quranDb.thikrDao.addAdhkars(adhkars)
    }

    override suspend fun searchAyahByText(searchText: String): List<AyahForSearchEntity> {
        return quranDb.ayahDao.searchAyahByText(searchText)
    }

    override fun getPageBySurahNumAndAyahNum(surahIndex: Int, ayahIndex: Int): Int {
        return quranDb.ayahDao.getPageBySurahNumAndAyahNum(surahIndex, ayahIndex)
    }

    override fun getSurahByPage(page: Int): String {
        return quranDb.ayahDao.getSurahInPage(page) ?: "الفاتحة"
    }

    override fun getAyahBySurahNumAndAyahNum(surahIndex: Int, ayahIndex: Int): String {
        return quranDb.ayahDao.getAyahBySurahNumAndAyahNum(surahIndex,ayahIndex)
    }

    fun getSurahNames(page : Int) : List<String>{
        return quranDb.ayahDao.getSurahNames(page)
    }


    fun getCount() = quranDb.thikrDao.getCount()
}