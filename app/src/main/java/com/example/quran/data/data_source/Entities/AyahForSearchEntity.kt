package com.example.quran.data.data_source.Entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "ayahs_for_search" ,indices = [Index(value = ["text"])])
data class AyahForSearchEntity (
    @PrimaryKey(autoGenerate = true)
    val id : Int? = null ,
    val num: String,
    val text: String ,
    val surahName: String,
    val surahNum: String
)
