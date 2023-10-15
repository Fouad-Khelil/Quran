package com.example.quran.data.data_source.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "surahs")
data class SurahEntity(
    @PrimaryKey(autoGenerate = false)
    val number: Int,
    val numberOfAyah : Int ,
    val englishName: String,
    val englishNameTranslation: String,
    val name: String,
    val revelationType: String ,
//    val lastReading: Date
)
