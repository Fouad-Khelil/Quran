package com.example.quran.exoplayer

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.quran.others.Constants.NETWORK_FAILURE
import com.example.quran.others.Event
import com.example.quran.others.Resource
import androidx.compose.runtime.State

class QuranServiceConnection(
    context: Context
) {
    private val _isConnected = MutableLiveData<Event<Resource<Boolean>>>()
    val isConnected: LiveData<Event<Resource<Boolean>>> = _isConnected

    private val _networkFailure = MutableLiveData<Event<Resource<Boolean>>>()
    val networkFailure: LiveData<Event<Resource<Boolean>>> = _networkFailure

    private val _playbackState = mutableStateOf<PlaybackStateCompat?>(null)
    val playbackState: State<PlaybackStateCompat?> = _playbackState

    var currentPlayingSora = mutableStateOf<MediaMetadataCompat?>(null)

    private val _nowPlaying = MutableLiveData<MediaMetadataCompat?>()
    val nowPlaying: LiveData<MediaMetadataCompat?> = _nowPlaying

    private lateinit var mediaController: MediaControllerCompat

    val transportController: MediaControllerCompat.TransportControls
        get() = mediaController.transportControls

    private val mediaBrowserConnectionCallback = MediaBrowserConnectionCallback(context)
    private val mediaBrowser = MediaBrowserCompat(
        context,
        ComponentName(
            context,
            QuranService::class.java
        ),
        mediaBrowserConnectionCallback,
        null
    ).apply { connect() }

    fun subscribe(parentId: String, callback: MediaBrowserCompat.SubscriptionCallback) {
        mediaBrowser.subscribe(parentId, callback)
    }

    fun unsubscribe(parentId: String, callback: MediaBrowserCompat.SubscriptionCallback) {
        mediaBrowser.unsubscribe(parentId, callback)
    }

    private inner class MediaBrowserConnectionCallback(
        private val context: Context
    ) : MediaBrowserCompat.ConnectionCallback() {

        override fun onConnected() {
            mediaController = MediaControllerCompat(context, mediaBrowser.sessionToken).apply {
                registerCallback(MediaControllerCallback())
            }
            _isConnected.postValue(
                Event(
                    Resource.Success(true)
                )
            )
        }

        override fun onConnectionSuspended() {
            _isConnected.postValue(
                Event(
                    Resource.Error(
                        "The connection was suspended",
                        false
                    )
                )
            )
        }

        override fun onConnectionFailed() {
            _isConnected.postValue(
                Event(
                    Resource.Error(
                        "Couldn't connect to media browser",
                        false
                    )
                )
            )
        }
    }

    private inner class MediaControllerCallback : MediaControllerCompat.Callback() {

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            _playbackState.value=state
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            super.onMetadataChanged(metadata)
            currentPlayingSora.value = metadata
            _nowPlaying.postValue(metadata)
        }

        override fun onSessionEvent(event: String?, extras: Bundle?) {
            super.onSessionEvent(event, extras)
            when (event) {
                NETWORK_FAILURE -> _networkFailure.postValue(
                    Event(
                        Resource.Error(
                            "Couldn't connect to the server. Please check your internet connection.",
                            null
                        )
                    )
                )
            }
        }

        override fun onSessionDestroyed() {
            mediaBrowserConnectionCallback.onConnectionSuspended()
        }
    }
}