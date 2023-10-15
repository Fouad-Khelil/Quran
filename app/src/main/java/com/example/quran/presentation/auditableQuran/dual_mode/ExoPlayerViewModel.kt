package com.example.quran.presentation.auditableQuran.dual_mode

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quran.data.repository.QuranAndThikrRepositoryImpl
import com.example.quran.others.AyahPrettyTextTransformer
import com.example.quran.others.Constants
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@Suppress("DEPRECATION")
@HiltViewModel
class ExoPlayerViewModel @Inject constructor(
    val context: Context,
    val quranRepo: QuranAndThikrRepositoryImpl
) : ViewModel() {

    // Initialize ExoPlayer and ConcatenatingMediaSource
    val exoPlayer = SimpleExoPlayer.Builder(context).build()
    val concatenatingMediaSource = ConcatenatingMediaSource()

    var currentTrackIndex = mutableStateOf(0)
        private set

    var currentAyah = mutableStateOf("")
        private set


    lateinit var eventListener: Player.EventListener


    fun setCurrentTrack(reciterIndex: Int) {
        val surahIndex = Constants.DUAL_LIST[reciterIndex].surahIndex

        viewModelScope.launch(Dispatchers.IO) {
            if (currentTrackIndex.value > 0 && currentTrackIndex.value <= Constants.DUAL_LIST[reciterIndex].numberOfAyahs) {
                currentAyah.value = AyahPrettyTextTransformer.removeUnsupportedChars(
                    quranRepo.getAyahBySurahNumAndAyahNum(
                        surahIndex,
                        currentTrackIndex.value
                    )
                )
            }
        }

    }


    private fun registerPlayerListener() {
        eventListener = object : Player.EventListener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                currentTrackIndex.value ++
            }
        }
        exoPlayer.addListener(eventListener)
    }

    init {
        registerPlayerListener()
    }

    override fun onCleared() {
        exoPlayer.removeListener(eventListener)
    }
}

