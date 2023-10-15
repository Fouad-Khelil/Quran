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
    fun appendAyahEndDecorator(ayah: AyahEntity): String {
        val sb = StringBuilder(ayah.text)
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