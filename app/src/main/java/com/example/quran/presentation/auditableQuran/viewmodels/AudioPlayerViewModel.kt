package com.example.quran.presentation.auditableQuran.viewmodels


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.quran.exoplayer.QuranService
import com.example.quran.exoplayer.QuranServiceConnection
import com.example.quran.exoplayer.currentPlaybackPosition
import com.example.quran.others.Constants.UPDATE_PLAYER_POSITION_INTERVAL
import com.example.quran.others.formatLong
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AudioPlayerViewModel @Inject constructor(
    quranServiceConnection: QuranServiceConnection
) : ViewModel() {

    private val playbackState = quranServiceConnection.playbackState

    var currentPlaybackPosition by mutableStateOf(0L)

    val currentPlayerPosition: Float
        get() {
            if (currentSoraDuration > 0) {
                return currentPlaybackPosition.toFloat() / currentSoraDuration
            }
            return 0f
        }

    val currentPlaybackFormattedPosition: String
        get() = formatLong(currentPlaybackPosition)

    val currentSoraFormattedPosition: String
        get() = formatLong(currentSoraDuration)


    val currentSoraDuration: Long
        get() = QuranService.currentSoraDuration

    suspend fun updateCurrentPlaybackPosition() {
        val currentPosition = playbackState.value?.currentPlaybackPosition
        if (currentPosition != null && currentPosition != currentPlaybackPosition) {
            currentPlaybackPosition = currentPosition
        }
        delay(UPDATE_PLAYER_POSITION_INTERVAL)
        updateCurrentPlaybackPosition()
    }

}