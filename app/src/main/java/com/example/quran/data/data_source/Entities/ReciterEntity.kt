package com.example.quran.data.data_source.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "reciter")
data class ReciterEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String,
    val photo: String,
    val rewayah: String,
    val server: String,
    val surah_list: String,
    val surah_total: Int,
    var is_favorite: Boolean
)