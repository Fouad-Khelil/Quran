package com.example.quran.data.repository

import com.example.quran.data.data_source.Entities.NameOfAllahEntity
import com.example.quran.data.data_source.local.qurandb.QuranDb
import javax.inject.Inject


class NamesOfAllahRepositoryImpl @Inject constructor(
    val db: QuranDb
) : NamesOfAllahRepository {
    override suspend fun addAllNames(namesOfAllah: List<NameOfAllahEntity>) {
        db.namesOfAllahDao.addAllNames(namesOfAllah)
    }

    override suspend fun getAllNames(): List<NameOfAllahEntity> {
        return  db.namesOfAllahDao.getAllNames()
    }
}