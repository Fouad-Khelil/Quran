package com.example.quran.data.data_source.local.qurandb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quran.data.data_source.Entities.AyahEntity
import com.example.quran.data.data_source.Entities.AyahForSearchEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface AyahDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllAyahs(ayahs: List<AyahEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllForSearchAyahs(ayahs: List<AyahForSearchEntity>)

    @Query("SELECT * FROM ayahs WHERE page = (SELECT page FROM ayahs WHERE number = :ayahNumber) order by number asc")
    fun getPageOfAyahIndex(ayahNumber : Int) : Flow<List<AyahEntity>>

    /**
     * when paging
     */
    @Query("SELECT * FROM ayahs WHERE page = :page order by number asc" )
    fun getPageByPageNumber(page : Int) : List<AyahEntity>

    @Query("SELECT surahs.name FROM surahs JOIN ayahs ON surahs.number = ayahs.surahIndex WHERE ayahs.page = :page ORDER BY ayahs.number ASC LIMIT 1")
    fun getSurahByPage(page : Int) : String?


    //when searching for ayah
    @Query("SELECT * FROM ayahs WHERE text LIKE :searchText order by number asc" )
    fun getSearchedAyah(searchText : String) : Flow<List<AyahEntity>>

    /**
     * when click a searched aya
     */
    @Query("SELECT * FROM ayahs WHERE number = :index")
    fun getAyahByIndex(index : Int) : Flow<AyahEntity>

    @Query("SELECT * FROM ayahs WHERE page = (SELECT MIN(page) FROM ayahs WHERE surahIndex = :surahIndex)")
    fun getSurahFirstPage(surahIndex : Int) : List<AyahEntity>

    @Query("SELECT * FROM ayahs WHERE text = :index")
    fun getAyahTafseer(index : Int) : Flow<List<AyahEntity>>


    @Query("SELECT * FROM ayahs_for_search WHERE text LIKE '%' || :searchText || '%'")
    suspend fun searchAyahByText(searchText: String): List<AyahForSearchEntity>

    @Query("SELECT page FROM ayahs WHERE surahIndex= :surahIndex and numberInSurah= :ayahIndex limit 1")
    fun getPageBySurahNumAndAyahNum(surahIndex : Int , ayahIndex :Int) : Int

    @Query("SELECT text FROM ayahs WHERE surahIndex= :surahIndex and numberInSurah= :ayahIndex limit 1")
    fun getAyahBySurahNumAndAyahNum(surahIndex : Int , ayahIndex :Int) : String

}