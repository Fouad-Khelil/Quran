package com.example.quran.data.data_source.local.dataClasses.quran

data class Surah(
    val ayahs : List<Ayah>,
    val englishName: String,
    val englishNameTranslation: String,
    val name: String,
    val number: Int,
    val revelationType: String
)