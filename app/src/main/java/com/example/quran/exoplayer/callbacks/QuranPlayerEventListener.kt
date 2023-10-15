package com.example.quran.exoplayer.callbacks

import android.widget.Toast
import com.example.quran.exoplayer.QuranService
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player

@Suppress("DEPRECATION")
class QuranPlayerEventListener(
    private val quranService: QuranService
) : Player.Listener {

    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
        super.onPlayWhenReadyChanged(playWhenReady, reason)
        if (reason == Player.STATE_READY && !playWhenReady) {
            quranService.stopForeground(false)
        }
    }

    override fun onPlayerError(error: ExoPlaybackException) {
        super.onPlayerError(error)
        Toast.makeText(quranService, "An unknown error", Toast.LENGTH_LONG).show()
    }
}