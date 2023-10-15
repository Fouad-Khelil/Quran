package com.example.quran.exoplayer

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.media.MediaBrowserServiceCompat
import com.example.quran.exoplayer.callbacks.QuranPlaybackPrepared
import com.example.quran.exoplayer.callbacks.QuranPlayerEventListener
import com.example.quran.exoplayer.callbacks.QuranPlayerNotificationListener
import com.example.quran.others.Constants.MEDIA_ROOT_ID
import com.example.quran.others.Constants.NETWORK_FAILURE
import com.example.quran.others.Constants.SERVICE_TAG
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class QuranService : MediaBrowserServiceCompat() {

    @Inject
    lateinit var dataSourceFactory: DefaultDataSourceFactory

    @Inject
    lateinit var exoPlayer: SimpleExoPlayer

    @Inject
    lateinit var quranSource: QuranSource

    private lateinit var quranNotificationManger: QuranNotificationManger

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    private lateinit var mediaSessionCompat: MediaSessionCompat
    private lateinit var mediaSessionConnector: MediaSessionConnector

    var isForegroundService = false

    private var currentPlayingSora: MediaMetadataCompat? = null

    private var isPlayerInitialize = false

    private lateinit var quranPlayerListener: QuranPlayerEventListener

    companion object {
        private const val TAG = "MediaPlayerService"

        var currentSoraDuration = 0L
            private set
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val reciterId = intent?.getIntExtra("reciter", 0)

        serviceScope.launch {

            if (reciterId != 0) {
                Log.d("myservice", "myservice: $reciterId")
                quranSource.fetchMediaData(reciterId!!)
            }
        }
        return START_NOT_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        val flags = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_MUTABLE
        }
        val activityIntent = packageManager?.getLaunchIntentForPackage(packageName)?.let {
            PendingIntent.getActivity(this, 0, it, flags)
        }

        mediaSessionCompat = MediaSessionCompat(this, SERVICE_TAG).apply {
            setSessionActivity(activityIntent)
            isActive = true
        }

        sessionToken = mediaSessionCompat.sessionToken

        quranNotificationManger = QuranNotificationManger(
            this,
            mediaSessionCompat.sessionToken,
            QuranPlayerNotificationListener(this)
        ) {
            currentSoraDuration = exoPlayer.duration
        }

        val quranPlaybackPreparer = QuranPlaybackPrepared(quranSource) {
            currentPlayingSora = it
            preparePlayer(
                quranSource.surahs,
                it,
                true
            )
        }

        mediaSessionConnector = MediaSessionConnector(mediaSessionCompat)
        mediaSessionConnector.setPlaybackPreparer(quranPlaybackPreparer)
        mediaSessionConnector.setQueueNavigator(quranSoraQueueNavigator())
        mediaSessionConnector.setPlayer(exoPlayer)

        quranPlayerListener = QuranPlayerEventListener(this)
        exoPlayer.addListener(quranPlayerListener)
        quranNotificationManger.showNotification(exoPlayer)
    }


    private inner class quranSoraQueueNavigator : TimelineQueueNavigator(mediaSessionCompat) {
        override fun getMediaDescription(player: Player, windowIndex: Int): MediaDescriptionCompat {
            return quranSource.surahs[windowIndex].description
        }
    }

    private fun preparePlayer(
        surahs: List<MediaMetadataCompat>,
        itemToPlay: MediaMetadataCompat?,
        playNow: Boolean
    ) {
        val curSoraIndex = if (currentPlayingSora == null) 0 else surahs.indexOf(itemToPlay)
        exoPlayer.setMediaSource(quranSource.asMediaSource(dataSourceFactory))
        exoPlayer.prepare()
        exoPlayer.seekTo(curSoraIndex, 0L)
        exoPlayer.playWhenReady = playNow
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        exoPlayer.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        exoPlayer.removeListener(quranPlayerListener)
        exoPlayer.release()
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        return BrowserRoot(MEDIA_ROOT_ID, null)
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        when (parentId) {
            MEDIA_ROOT_ID -> {
                val resultsSent = quranSource.whenReady { isInitialized ->
                    if (isInitialized) {
                        result.sendResult(quranSource.asMediaItems())
                        if (!isPlayerInitialize && quranSource.surahs.isNotEmpty()) {
                            preparePlayer(quranSource.surahs, quranSource.surahs[0], false)
                            isPlayerInitialize = true
                        }
                    } else {
                        mediaSessionCompat.sendSessionEvent(NETWORK_FAILURE, null)
                        result.sendResult(null)
                    }
                }
                if (!resultsSent) {
                    result.detach()
                }
            }
        }
    }
}