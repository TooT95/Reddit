package com.example.reddit.extension

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.MediaController
import android.widget.SearchView
import android.widget.VideoView
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide
import com.example.reddit.R
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber

fun ViewGroup.inflateLayout(@LayoutRes layoutId: Int, attachToParent: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToParent)
}

fun ImageView.glideImageWithParams(view: View, url: String) {
    Glide.with(view)
        .load(url)
        .placeholder(R.drawable.ic_avatar)
        .into(this)
}

fun PlayerView.setUrlAndMediaPlayer(context: Context, url: String) {
    player = ExoPlayer.Builder(context).build()
    player?.run {
        val mediaItem = MediaItem.Builder()
            .setUri(url)
            .build()
        setMediaItem(mediaItem)
        prepare()
        player
    }
}

fun SearchView.changeFlow(): Flow<String?> {
    return callbackFlow {
        val queryTextListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(text: String?): Boolean {
                text?.let {
                    if (it.isEmpty()) {
                        trySendBlocking(null)
                    } else {
                        trySendBlocking(it)
                    }
                }

                return false
            }
        }
        this@changeFlow.setOnQueryTextListener(queryTextListener)
        awaitClose {
            setOnQueryTextListener(null)
        }
    }
}