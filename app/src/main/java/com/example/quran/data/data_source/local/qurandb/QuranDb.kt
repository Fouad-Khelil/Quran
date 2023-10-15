package com.example.quran.data.data_source.local.qurandb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.quran.data.data_source.Entities.AdhkarEntity
import com.example.quran.data.data_source.Entities.AyahEntity
import com.example.quran.data.data_source.Entities.AyahForSearchEntity
import com.example.quran.data.data_source.Entities.NameOfAllahEntity
import com.example.quran.data.data_source.Entities.ReciterEntity
import com.example.quran.data.data_source.Entities.SurahEntity

@Database(
    entities = [
        AyahEntity::class,
        SurahEntity::class,
        AdhkarEntity::class,
        ReciterEntity::class,
        AyahForSearchEntity::class,
        NameOfAllahEntity::class
    ],
    version = 6,
    exportSchema = false
)
abstract class QuranDb : RoomDatabase() {
    abstract val ayahDao: AyahDao
    abstract val surahDao: SurahDao
    abstract val thikrDao: AdhkarDao
    abstract val reciterDao: ReciterDao
    abstract val namesOfAllahDao: NamesOfAllahDao

    companion object {
        const val DATABASE_NAME = "zad_db"
    }
}