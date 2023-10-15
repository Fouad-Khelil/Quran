package com.example.quran.navigation


import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.example.quran.R

sealed class ItemBottomNav(
    val title : String,
    val icon : Int ,
    val route : String
) {
    object Home : ItemBottomNav("Home",  R.drawable.ic_round_home_24,"home")
    object Doa : ItemBottomNav("doa", R.drawable.ic_doa_,"doa")
    object ReadableQuran : ItemBottomNav("Readable Quran",  R.drawable.ic_quran,"quran_r")
    object AuditableQuran : ItemBottomNav("Auditable Quran", R.drawable.ic_sound,"quran_a")
    object Others : ItemBottomNav("Others",  R.drawable.other_apps,"others")
}

sealed class Screen(val route: String) {
    object SplashScreen : Screen("splash")
    object QuranPaperScreen : Screen("quran_paper?surah_index={surah_index}&page={page}") {
        fun createRoute(surahIndex : Int): String = "quran_paper?surah_index=$surahIndex"
        fun pageRoute(surahIndex : Int,page : Int): String = "quran_paper?surah_index=$surahIndex&page=$page"
    }
    object AllSoraAudioScreen : Screen("all_sora_audio/{reciter_name}/{rewayah}/{photo}/{server}/{surah_list}") {
        fun createRoute(
            reciterName: String ,
            rewayah: String ,
            photo : String ,
            server : String ,
            surahList : String
        ): String = "all_sora_audio/$reciterName/$rewayah/$photo/$server/$surahList"
    }
    object AudioPlayerScreen : Screen("audio_player/{rewayah}"){
        fun createRoute(rewayah: String): String = "audio_player/$rewayah"
    }
    object AudioDualScreen : Screen("audio_dual/{reciter_index}"){
        fun createRoute(index: Int): String = "audio_dual/$index"
    }
    object RequestLocationScreen : Screen("location")
    object ThikrDetailScreen : Screen("thikr_detail_screen/{category}") {
        fun createRoute(category: String): String = "thikr_detail_screen/$category"
    }

    object QiblaCompassScreen : Screen("qibla")
    object HadithScreen : Screen("hadith")
    object NamesOfAllahScreen : Screen("names_of_allah")
    object SettingsScreen : Screen("settings")
    object AyahSearchScreen : Screen("search_screen")
    object AboutTheAppScreen : Screen("about_the_app")
    object NotificationScreen : Screen("notifications")

    object LocationSettingScreen : Screen("location_setting")



}
