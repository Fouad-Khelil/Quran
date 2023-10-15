package com.example.quran.data.data_source.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "thikr")
data class AdhkarEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Int? = null ,
    val category: String,
    val count: String,
    val description: String,
    val reference: String,
    val zekr: String
)