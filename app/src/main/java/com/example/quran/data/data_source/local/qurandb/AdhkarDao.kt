package com.example.quran.data.data_source.local.qurandb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quran.data.data_source.Entities.AdhkarEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface AdhkarDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAdhkars(adhkars: List<AdhkarEntity>)

    @Query("SELECT * FROM thikr WHERE id = :index")
    fun getThikrByIndex(index: Int): Flow<AdhkarEntity>

    @Query("SELECT COUNT(*) FROM thikr")
    fun getCount(): Flow<Int>

    @Query(
        "SELECT *\n" +
                "FROM thikr\n" +
                "WHERE CASE\n" +
                "  WHEN :isItInResquestedList = 1 THEN category IN (:requestedList)\n" +
                "  ELSE category NOT IN (:excludedList)\n" +
                "END"
    )
    suspend fun getAllThikr(
        isItInResquestedList: Boolean,
        requestedList: List<String>,
        excludedList: List<String>
    ): List<AdhkarEntity>

}