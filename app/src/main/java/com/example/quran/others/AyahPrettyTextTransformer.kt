package com.example.quran.others

import com.example.quran.data.data_source.Entities.AyahEntity
import java.text.MessageFormat
import java.util.*

object AyahPrettyTextTransformer {

    fun removeUnsupportedChars(text: String): String {
        val regex = "[" + 1759.toChar() + 1763.toChar() + 1771.toChar() + "]"
        return text.replace(regex.toRegex(), "")
    }

    //appendSpecialEndDecorators
    fun addDecoratorsToAyah(ayah: AyahEntity): String {

        // adding the t separator to split using it the beginning of each ayah
        val ayahWithSeparator =
            if (ayah.numberInSurah == 1) {
                "\t" +
                        if (ayah.surahIndex == Constants.SURAH_ALFATIHA || ayah.surahIndex == Constants.SURAH_ALTAWBA) ayah.text
                        else ayah.text.removePrefix(Constants.BASMALAH)
            } else {
                ayah.text
            }

        val sb = StringBuilder(ayahWithSeparator)
        sb.append(
            MessageFormat.format(
                " {0} \n",
                getArabicNumber(ayah.numberInSurah)
            )
        )
        return sb.toString()
    }

    //    fun appendAyahEndDecorator(ayah: AyahEntity, nextAyahEntity: AyahEntity): String {
    fun getArabicNumber(num: Int): String {
        val locale = Locale("ar")
        return String.format(locale, "%d", num)
    }

}