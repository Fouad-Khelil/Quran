package com.example.quran.data.data_source.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "names_of_allah")
data class NameOfAllahEntity(
    val description: String,
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String
)