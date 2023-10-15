package com.example.quran.data.repository

import com.example.quran.data.data_source.Entities.ReciterEntity
import com.example.quran.data.data_source.local.qurandb.QuranDb
import com.example.quran.exoplayer.Sora
import com.example.quran.others.formatLink
import javax.inject.Inject

class AudioRepository @Inject constructor(
    val db: QuranDb
) {
//    suspend fun getAllReciters(): List<ReciterEntity> {
//        return db.reciterDao.getAllReciters()
//    }
    suspend fun getReciter(reciterId: Int): ReciterEntity {
        return db.reciterDao.getReciter(reciterId)
    }

    suspend fun getAllSurahas(): List<String> {
        return db.surahDao.getAllSurahsNames()
    }

    suspend fun getAllSora(reciterId: Int): List<Sora> {
        var surahWithReciter: ArrayList<Sora> = arrayListOf()

        val reciter = getReciter(reciterId)
        val surahNames = getAllSurahas()
//        reciters.forEachIndexed { ind, reciter ->
        val surahs = reciter.surah_list.split(",").map { it.toInt() }.mapIndexed { index, item ->
            Sora(
                name = surahNames[item - 1],
                reciter = reciter.name,
                mediaId = reciter.name + item.toString(),
                soraUrl = formatLink(item, reciter.server),
                imageUrl = reciter.photo
            )
        }
        surahWithReciter.addAll(surahs)
//        }
        return surahWithReciter
    }
}