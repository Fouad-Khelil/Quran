package com.example.quran.exoplayer

import android.support.v4.media.MediaMetadataCompat

fun MediaMetadataCompat.toSora(): Sora? {
    return description?.let {
        Sora(
            mediaId = it.mediaId ?: "",
            name = it.title.toString(),
            reciter = it.subtitle.toString(),
            soraUrl = it.mediaUri.toString(),
            imageUrl = it.iconUri.toString()
        )
    }
}