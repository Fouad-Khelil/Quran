package com.example.quran.data.repository

import com.example.quran.data.data_source.Entities.NameOfAllahEntity

interface NamesOfAllahRepository {

    suspend fun addAllNames(namesOfAllah: List<NameOfAllahEntity>)
    suspend fun getAllNames(): List<NameOfAllahEntity>

}