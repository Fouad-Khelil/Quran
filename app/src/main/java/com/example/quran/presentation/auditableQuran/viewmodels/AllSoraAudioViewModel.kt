package com.example.quran.presentation.auditableQuran.viewmodels


import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_MEDIA_ID
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.quran.data.repository.DataStoreRepository
import com.example.quran.exoplayer.*
import com.example.quran.others.Constants.MEDIA_ROOT_ID
import com.example.quran.others.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class AllSoraAudioViewModel @Inject constructor(
    val dataStoreRepository: DataStoreRepository,
    private val quranServiceConnection: QuranServiceConnection
) : ViewModel() {

    var mediaItems = mutableStateOf<Resource<List<Sora>>>(Resource.Loading(null))

    var showPlayerFullScreen by mutableStateOf(false)

    val isConnected = quranServiceConnection.isConnected
    val networkError = quranServiceConnection.networkFailure
    val curPlayingSora = quranServiceConnection.nowPlaying

    val currentPlayingSora = quranServiceConnection.currentPlayingSora

    val soraIsPlaying: Boolean
        get() = playbackState.value?.isPlaying == true

    val playbackState = quranServiceConnection.playbackState


    fun init(reciter: String) {
        mediaItems.value = (Resource.Loading(null))
        quranServiceConnection.subscribe(
            MEDIA_ROOT_ID,
            object : MediaBrowserCompat.SubscriptionCallback() {
                override fun onChildrenLoaded(
                    parentId: String,
                    children: MutableList<MediaBrowserCompat.MediaItem>
                ) {
                    super.onChildrenLoaded(parentId, children)
                    val items = children.filter {
                        it.description.subtitle == reciter
                    }.map {
                        Sora(
                            it.mediaId!!,
                            it.description.title.toString(),
                            it.description.subtitle.toString(),
                            it.description.mediaUri.toString(),
                            it.description.iconUri.toString()
                        )
                    }
                    mediaItems.value = Resource.Success(items)
                }
            })
    }


    fun skipToNextSora() {
        quranServiceConnection.transportController.skipToNext()
    }

    fun skipToPreviousSora() {
        quranServiceConnection.transportController.skipToPrevious()
    }

    fun seekTo(pos: Float) {
        quranServiceConnection.transportController.seekTo(pos.toLong())
    }

    fun repeatSoraOnce(repeatMode: Boolean) {
        if (repeatMode) {
            quranServiceConnection.transportController.setRepeatMode(PlaybackStateCompat.REPEAT_MODE_ONE)
        } else {
            quranServiceConnection.transportController.setRepeatMode(PlaybackStateCompat.REPEAT_MODE_NONE)
        }
    }


    fun playOrToggleSora(mediaItem: Sora, toggle: Boolean = false) {
        val isPrepared = playbackState.value?.isPrepared ?: false
        if (isPrepared && mediaItem.mediaId ==
            currentPlayingSora.value?.getString(METADATA_KEY_MEDIA_ID)
//            curPlayingSora.value?.getString(METADATA_KEY_MEDIA_ID)
        ) {
            playbackState.value?.let { playbackState ->
                when {
                    playbackState.isPlaying -> {
                        if (toggle) quranServiceConnection.transportController.pause()
                    }
                    playbackState.isPlayEnabled -> {
                        quranServiceConnection.transportController.play()
                    }
                    else -> Unit
                }
            }
        } else {
            quranServiceConnection.transportController.playFromMediaId(mediaItem.mediaId, null)
        }
    }


    override fun onCleared() {
        super.onCleared()
        quranServiceConnection.unsubscribe(
            MEDIA_ROOT_ID,
            object : MediaBrowserCompat.SubscriptionCallback() {})
        val surahOrder = currentPlayingSora.value?.toSora()!!.mediaId.takeLastWhile { it.isDigit() }.toInt()
        dataStoreRepository.saveLastListening(surahOrder)
        // todo call the datasotre here
    }
}