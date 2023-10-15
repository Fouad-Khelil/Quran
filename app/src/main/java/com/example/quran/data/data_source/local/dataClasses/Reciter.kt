package com.example.quran.data.data_source.local.dataClasses

data class Reciter(
    val id: Int,
    val name: String,
    val photo: String,
    val rewayah: String,
    val server: String,
    val surah_list: String,
    val surah_total: Int
)