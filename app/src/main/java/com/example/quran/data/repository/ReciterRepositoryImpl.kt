package com.example.quran.data.repository

import com.example.quran.data.data_source.Entities.ReciterEntity
import com.example.quran.data.data_source.local.qurandb.QuranDb
import javax.inject.Inject


class ReciterRepositoryImpl @Inject constructor(
    val db: QuranDb
) : ReciterRepository {

    override suspend fun addAllReciters(reciters: List<ReciterEntity>) {
        db.reciterDao.addAllReciters(reciters)
    }

    override suspend fun getAllReciters(): List<ReciterEntity> {
        return db.reciterDao.getAllReciters()
    }

    override suspend fun markAsFavorite(reciter: ReciterEntity) {
        db.reciterDao.updateReciter(reciter)
    }

    override suspend fun getFavoriteReciters(): List<ReciterEntity> {
        return db.reciterDao.getFavoriteReciters()
    }
}