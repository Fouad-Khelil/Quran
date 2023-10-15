package com.example.quran.data.repository


import android.content.Context
import com.example.quran.data.data_source.Entities.ReciterEntity
import com.example.quran.others.Constants
import com.example.quran.others.getAddressFromLatLngWithTimeout
import com.example.quran.presentation.splash.LocationDetail


//private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings_pref")


class DataStoreRepository(context: Context) {

    private val sharedPreferences =
        context.getSharedPreferences("shared_preference_file", Context.MODE_PRIVATE)

    companion object {
        private const val IS_DB_BUILT = "is_db_built"
        private const val LAST_READING_PAGE = "last_reading_page"
        private const val LAST_LISTENING_ID = "last_listening_id"
        private const val IS_DARK_MODE = "is_dark_mode"
        private const val IS_FIRST_TIME = "is_first_time"
        private const val LATITUDE = "latitude"
        private const val LONGTITUDE = "longtitude"
        private const val LOCATION_NAME = "LOCATION_NAME"
        private const val FONT_SIZE = "FONT"
        private const val APP_BAR_SETTING = "APP_BAR_SETTING"
        private const val CALCULATION_METHOD = "CALCULATION_METHOD"


        //*******************reciter **************
        private const val RECITER_ID = "id"
        private const val RECITER_NAME = "reicter_name"
        private const val RECITER_PHOTO = "reciter_photo"
        private const val RECITER_REWAYAH = "reciter_rewayah"
        private const val RECITER_SERVER = "reciter_server"
        private const val RECITER_SURAH_LIST = "reciter_surah_list"
        private const val RECITER_SURAH_TOTAL = "reciter_surah_total"
        //*****************************************
    }

    fun saveIsDbBuilt(value: Boolean) {
        sharedPreferences.edit().putBoolean(IS_DB_BUILT, value).apply()
    }

    fun getIsDbBuilt(): Boolean {
        return sharedPreferences.getBoolean(IS_DB_BUILT, false)
    }

    fun setIsFirstTime(value: Boolean) {
        sharedPreferences.edit().putBoolean(IS_FIRST_TIME, value).apply()
    }

    fun getIsFirstTime(): Boolean {
        return sharedPreferences.getBoolean(IS_FIRST_TIME, true)
    }

    fun setAppBarSetting(value: Boolean) {
        sharedPreferences.edit().putBoolean(APP_BAR_SETTING, value).apply()
    }

    fun getAppBarSetting(): Boolean {
        return sharedPreferences.getBoolean(APP_BAR_SETTING, true)
    }

    fun setCalculationMethod(value: String) {
        sharedPreferences.edit().putString(CALCULATION_METHOD, value).apply()
    }

    fun getCalculationMethod(): String {
        return sharedPreferences.getString(CALCULATION_METHOD, Constants.ISLAMIC_LEAGUE_METHOD) ?:Constants.NORTH_AMERICA_METHOD
    }


    fun setTheme(value: Int) {
        sharedPreferences.edit().putInt(IS_DARK_MODE, value).apply()
    }

    fun getTheme(): Int {
        return sharedPreferences.getInt(IS_DARK_MODE, Constants.LIGHT_THEME)
    }

//    fun setFont(value: Int) {
//        sharedPreferences.edit().putInt(IS_DARK_MODE, value).apply()
//    }
//
//    fun getFont(): Int {
//        return sharedPreferences.getInt(IS_DARK_MODE, Constants.LIGHT_THEME)
//    }


    fun setLatAndLng(lat: Double, lng: Double) {
        sharedPreferences.edit().apply {
            putString(LATITUDE, lat.toString())
            putString(LONGTITUDE, lng.toString())
            putString(LONGTITUDE, lng.toString())
        }.apply()
    }

    suspend fun getLocationName(context: Context) :String {
        val lat =sharedPreferences.getString(LATITUDE, "") ?: ""
        val lng =sharedPreferences.getString(LONGTITUDE, "") ?: ""
        return getAddressFromLatLngWithTimeout(lat.toDouble(),lng.toDouble(),context)
    }

    fun getLatAndLng(): LocationDetail {
        val lat = (sharedPreferences.getString(LATITUDE, "36.0"))?.toDouble()
        val lng = (sharedPreferences.getString(LONGTITUDE, "3.3"))?.toDouble()
        return LocationDetail(lat!!, lng!!)
    }

    fun setLastReadingPage(page: Int) {
        sharedPreferences.edit().putInt(LAST_READING_PAGE, page).apply()
    }

    fun getLastReadingPage(): Int {
        return sharedPreferences.getInt(LAST_READING_PAGE, 1)
    }

    fun setFontSize(fontSize: Float) {
        sharedPreferences.edit().putFloat(FONT_SIZE, fontSize).apply()
    }

    fun getFontSize(): Float {
        return sharedPreferences.getFloat(FONT_SIZE, Constants.INITIAL_FONT_SIZE)
    }


    fun saveLastListening(surahOrder: Int) {
        sharedPreferences.edit().putInt(LAST_LISTENING_ID, surahOrder).apply()
    }

    fun getLastListeningSurah(): Int {
        return sharedPreferences.getInt(LAST_LISTENING_ID, 1)
    }


    fun saveLastReciter(reciterEntity: ReciterEntity) {
        sharedPreferences.edit().apply {
            putInt(RECITER_ID, reciterEntity.id)
            putString(RECITER_NAME, reciterEntity.name).apply()
            putString(RECITER_PHOTO, reciterEntity.photo).apply()
            putString(RECITER_REWAYAH, reciterEntity.rewayah).apply()
            putString(RECITER_SERVER, reciterEntity.server).apply()
            putString(RECITER_SURAH_LIST, reciterEntity.surah_list).apply()
            putInt(RECITER_SURAH_TOTAL, reciterEntity.surah_total).apply()
        }.apply()
    }

    //------------------last actions--------------------




    //---------------------------------

    fun getLastReciter() = ReciterEntity(
        sharedPreferences.getInt(RECITER_ID, 1),
        sharedPreferences.getString(RECITER_NAME, "إبراهيم الأخضر") ?: "",
        sharedPreferences.getString(
            RECITER_PHOTO,
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRwZ_TDEoW_914sjvHbP6xBLq1fRZlpMynTaXGC7aum209bpJrSBUk4MSyeHgo&s"
        ) ?: "",
        sharedPreferences.getString(RECITER_REWAYAH, "حفص عن عاصم - مرتل") ?: "",
        sharedPreferences.getString(RECITER_SERVER, "https://server6.mp3quran.net/akdr/") ?: "",
        sharedPreferences.getString(
            RECITER_SURAH_LIST,
            "1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114"
        ) ?: "",
        sharedPreferences.getInt(RECITER_SURAH_TOTAL, 114),
        is_favorite = false
    )


}
