package com.example.quran.exoplayer

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.MediaMetadataCompat.*
import com.example.quran.data.repository.AudioRepository
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class QuranSource @Inject constructor (
    private val audioRepository: AudioRepository
) {

    var surahs = emptyList<MediaMetadataCompat>()

    suspend fun fetchMediaData(reciterId : Int) = withContext(Dispatchers.Default) {
        state = State.STATE_INITIALIZING
        Dispatchers.IO
        val allReciterSurahs = audioRepository.getAllSora(reciterId)
        surahs = allReciterSurahs.map { sora ->
            MediaMetadataCompat.Builder()
                .putString(METADATA_KEY_TITLE, sora.name)
                .putString(METADATA_KEY_DISPLAY_TITLE, sora.name)
                .putString(METADATA_KEY_ARTIST, sora.reciter)
                .putString(METADATA_KEY_DISPLAY_SUBTITLE, sora.reciter)
                .putString(METADATA_KEY_MEDIA_ID, sora.mediaId)
                .putString(METADATA_KEY_MEDIA_URI, sora.soraUrl)
                .putString(METADATA_KEY_DISPLAY_ICON_URI, sora.imageUrl)
                .putString(METADATA_KEY_ALBUM_ART_URI, sora.imageUrl)
                .putString(METADATA_KEY_DISPLAY_DESCRIPTION, sora.name)
                .build()
        }
        Dispatchers.Main
        state = State.STATE_INITIALIZED
    }


    fun asMediaSource(dataSourceFactory: DefaultDataSourceFactory): ConcatenatingMediaSource {
        val concatenatingMediaSource = ConcatenatingMediaSource()
        surahs.forEach { sora ->
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(sora.getString(METADATA_KEY_MEDIA_URI)))
            concatenatingMediaSource.addMediaSource(mediaSource)
        }
        return concatenatingMediaSource
    }

    fun asMediaItems() = surahs.map { sora ->
        val desc = MediaDescriptionCompat.Builder()
            .setMediaUri(sora.description.mediaUri)
            .setTitle(sora.description.title)
            .setSubtitle(sora.description.subtitle)
            .setMediaId(sora.description.mediaId)
            .setIconUri(sora.description.iconUri)
            .build()
        MediaBrowserCompat.MediaItem(desc, FLAG_PLAYABLE)
    }.toMutableList()

    private val onReadyListeners = mutableListOf<(Boolean) -> Unit>()

    private var state: State = State.STATE_CREATED
        set(value) {
            if (value == State.STATE_INITIALIZED || value == State.STATE_ERROR) {
                synchronized(onReadyListeners) {
                    field = value
                    onReadyListeners.forEach { listener ->
                        listener(state == State.STATE_INITIALIZED)
                    }
                }
            }
        }

    fun whenReady(action: (Boolean) -> Unit): Boolean {
        return if (state == State.STATE_CREATED || state == State.STATE_INITIALIZING) {
            onReadyListeners += action
            false
        } else {
            action(state == State.STATE_INITIALIZED)
            true
        }
    }
}

enum class State {
    STATE_CREATED,
    STATE_INITIALIZING,
    STATE_INITIALIZED,
    STATE_ERROR
}

val fatihah = listOf(
    "https://everyayah.com/data/Ghamadi_40kbps/001001.mp3" ,
    "https://everyayah.com/data/Ghamadi_40kbps/001002.mp3" ,
    "https://everyayah.com/data/Ghamadi_40kbps/001003.mp3" ,
    "https://everyayah.com/data/Ghamadi_40kbps/001004.mp3" ,
    "https://everyayah.com/data/Ghamadi_40kbps/001005.mp3" ,
    "https://everyayah.com/data/Ghamadi_40kbps/001006.mp3" ,
    "https://everyayah.com/data/Ghamadi_40kbps/001007.mp3" ,
)