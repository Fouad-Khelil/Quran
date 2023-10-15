package com.example.quran.data.data_source.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "ayahs")
data class AyahEntity(
    @PrimaryKey
    val number: Int,
    val hizbQuarter: Int,
    val juz: Int,
    val manzil: Int,
    val numberInSurah: Int,
    val page: Int,
    val ruku: Int,
    val sajda: Boolean,
    var text: String,
    val tafseer: String = "",
    var surahIndex: Int = 0,
    val audioPath: String? = null
)

