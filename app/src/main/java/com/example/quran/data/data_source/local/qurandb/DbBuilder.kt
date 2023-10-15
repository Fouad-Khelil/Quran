package com.example.quran.data.data_source.local.qurandb

import android.content.Context
import android.util.Log
import com.example.quran.data.data_source.Entities.*
import com.example.quran.data.repository.NamesOfAllahRepository
import com.example.quran.data.repository.NamesOfAllahRepositoryImpl
import com.example.quran.data.repository.QuranAndThikrRepositoryImpl
import com.example.quran.data.repository.ReciterRepositoryImpl
import com.example.quran.others.Utils

class DbBuilder(
    val quranRepository: QuranAndThikrRepositoryImpl,
    val recitersRepository: ReciterRepositoryImpl,
    val namesOfAllahRepository: NamesOfAllahRepositoryImpl,
    val context: Context
) {

    suspend fun build() {
        val surahs = Utils.getAllQuranFromGson(context)?.data?.surahs
        val surahsForSearch = Utils.getAllQuranForSearchFromGson(context)?.surahs
        val tafseer = Utils.getTafseerFromGson(context)?.data?.surahs
        val reciters = Utils.getRecitersFromGson(context)
        val thikr = Utils.getAllThikrFromGson(context)
        val namesOfAllah = Utils.getNamesOfAllahFromGson(context)

        val ayahs = mutableListOf<AyahEntity>()
        val ayahsForSearch = mutableListOf<AyahForSearchEntity>()
        val thikrList = mutableListOf<AdhkarEntity>()
        val reciterList = mutableListOf<ReciterEntity>()
        // map it to db schema
        if (surahs != null && tafseer != null && thikr != null && reciters != null && surahsForSearch != null) {
            val surahsList = surahs.mapIndexed { surahIndex, surah ->

                // add ayahs and tafsir
                ayahs.addAll(surah.ayahs.mapIndexed { ayahIndex, ayah ->
                    AyahEntity(
                        number = ayah.number,
                        surahIndex = surah.number,
                        page = ayah.page,
                        juz = ayah.juz,
                        hizbQuarter = ayah.hizbQuarter,
                        sajda = false,
                        numberInSurah = ayah.numberInSurah,
                        text = ayah.text,
                        manzil = ayah.manzil,
                        ruku = ayah.ruku,
                        tafseer = tafseer[surahIndex].ayahs[ayahIndex].text
                    )
                })
                // create sura item
                SurahEntity(
                    surah.number,
                    surah.ayahs.size,
                    surah.englishName,
                    surah.englishNameTranslation,
                    surah.name,
                    surah.revelationType
                )
            }

            surahsForSearch.forEach { surah ->
                ayahsForSearch.addAll(
                    surah.ayahs.map { ayah ->
                        AyahForSearchEntity(
                            num = ayah.num,
                            text = ayah.text,
                            surahName = surah.name,
                            surahNum = surah.num,
                        )
                    }
                )
            }


            thikrList.addAll(thikr.map { myThikr ->
                AdhkarEntity(
                    category = myThikr.category,
                    count = myThikr.count,
                    description = myThikr.description,
                    reference = myThikr.reference,
                    zekr = myThikr.zekr
                )
            })

            reciterList.addAll(reciters.map { reciter ->
                ReciterEntity(
                    reciter.id,
                    reciter.name,
                    reciter.photo,
                    reciter.rewayah,
                    reciter.server,
                    reciter.surah_list,
                    reciter.surah_total,
                    is_favorite = false
                )
            })

            try {
                quranRepository.addSurahs(surahsList)
                quranRepository.addAllAyahs(ayahs)
                quranRepository.addAllForSeachAyahs(ayahsForSearch)
                quranRepository.addAdhkars(thikrList)
                recitersRepository.addAllReciters(reciterList)
                if (namesOfAllah!=null){
                    namesOfAllahRepository.addAllNames(namesOfAllah)
                }
            } catch (e: Exception) {
                val TAG = "db"
                Log.d(TAG, "exception")
            }
        }
    }
}