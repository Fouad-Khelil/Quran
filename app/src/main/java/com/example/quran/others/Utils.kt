@file:Suppress("DEPRECATION")

package com.example.quran.others

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.quran.data.data_source.Entities.NameOfAllahEntity
import com.example.quran.data.data_source.local.dataClasses.adhkar.Adhkar
import com.example.quran.data.data_source.local.dataClasses.quran.FullQuran
import com.example.quran.data.data_source.local.dataClasses.Reciter
import com.example.quran.data.data_source.local.dataClasses.quranForSearch.QuranForSearch
import com.example.quran.data.repository.DataStoreRepository
import com.example.quran.navigation.Screen
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    fun getAllQuranFromGson(context: Context): FullQuran? {
        try {
            val json = context.assets.open("quran.json").bufferedReader().use { it.readText() }
            return Gson().fromJson(json, object : TypeToken<FullQuran>() {}.type)
        } catch (e: IOException) {
            Log.e("MainActivity", "Error reading JSON file: ${e.message}")
            return null
        }
    }

    fun getAllQuranForSearchFromGson(context: Context): QuranForSearch? {
        try {
            val json =
                context.assets.open("quran_clean.json").bufferedReader().use { it.readText() }
            return Gson().fromJson(json, object : TypeToken<QuranForSearch>() {}.type)
        } catch (e: IOException) {
            Log.e("MainActivity", "Error reading JSON file: ${e.message}")
            return null
        }
    }

    fun getAllThikrFromGson(context: Context): List<Adhkar>? {
        try {
            val json = context.assets.open("azkar.json").bufferedReader().use { it.readText() }
            return Gson().fromJson(json, object : TypeToken<List<Adhkar>>() {}.type)
        } catch (e: IOException) {
            Log.e("MainActivity", "Error reading JSON file: ${e.message}")
            return null
        }
    }

    fun getTafseerFromGson(context: Context): FullQuran? {
        try {
            val json = context.assets.open("tafseer.json").bufferedReader().use { it.readText() }
            return Gson().fromJson(json, object : TypeToken<FullQuran>() {}.type)
        } catch (e: IOException) {
            Log.e("MainActivity", "Error reading JSON file: ${e.message}")
            return null
        }
    }


    fun getRecitersFromGson(context: Context): List<Reciter>? {
        try {
            val json = context.assets.open("reciters.json").bufferedReader().use { it.readText() }
            return Gson().fromJson(json, object : TypeToken<List<Reciter>?>() {}.type)
        } catch (e: IOException) {
            Log.e("MainActivity", "Error reading JSON file: ${e.message}")
            return null
        }
    }


    fun getNamesOfAllahFromGson(context: Context): List<NameOfAllahEntity>? {
        try {
            val json = context.assets.open("names_of_allah.json").bufferedReader().use { it.readText() }
            return Gson().fromJson(json, object : TypeToken<List<NameOfAllahEntity>?>() {}.type)
        } catch (e: IOException) {
            Log.e("MainActivity", "Error reading JSON file: ${e.message}")
            return null
        }
    }
}

fun formatLink(surahOrder: Int, serverLink: String): String {
    val addedString = if (surahOrder > 99) surahOrder.toString() else
        if (surahOrder > 9) "0" + surahOrder.toString() else "00" + surahOrder.toString()

    return serverLink + addedString + ".mp3"
}

fun isNotInHidenScreen(item: String): Boolean {

    val hidenScreens = listOf(
        Screen.QuranPaperScreen.route,
        Screen.SplashScreen.route,
        Screen.AudioDualScreen.route,
        Screen.AudioPlayerScreen.route,
        Screen.QiblaCompassScreen.route,
        Screen.AyahSearchScreen.route,
        Screen.AboutTheAppScreen.route,
        Screen.NamesOfAllahScreen.route,
    )
    return item !in hidenScreens
}

fun formatLong(value: Long): String {
    val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
    return dateFormat.format(value)
}


@Composable
fun isItInDarkTheme(): Boolean {
    val theme = DataStoreRepository(LocalContext.current).getTheme()
    return when (theme) {
        Constants.LIGHT_THEME -> false
        Constants.DARK_THEME -> true
        else -> {
            isSystemInDarkTheme()
        }
    }
}

fun Float.toRadians(): Float {
    return (this * Math.PI / 180).toFloat()
}

suspend fun getAddressFromLatLngWithTimeout(
    lat: Double,
    lng: Double,
    context: Context,
    timeoutMillis: Long = 1000L
): String {
    return withContext(Dispatchers.Default) {
        val geocoder = Geocoder(context, Locale("ar")) // Specify Arabic locale here
        var result = ""
        try {
            val addresses: List<Address>? = withTimeoutOrNull(timeoutMillis) {
                geocoder.getFromLocation(lat, lng, 1)
            }

            if (addresses != null && addresses.isNotEmpty()) {
                val address: Address = addresses[0]
                // Combine multiple address lines into a single string
                result += "${address.locality} , ${address.adminArea} ,${address.countryName}"
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        result
    }
}

fun colorMatrixWithBrightness(brightness: Float) = floatArrayOf(
    1f, 0f, 0f, 0f, brightness,
    0f, 1f, 0f, 0f, brightness,
    0f, 0f, 1f, 0f, brightness,
    0f, 0f, 0f, 1f, 0f
)
