package com.example.reddit.extension

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.MediaController
import android.widget.VideoView
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide
import com.example.reddit.R

fun ViewGroup.inflateLayout(@LayoutRes layoutId: Int, attachToParent: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToParent)
}

fun ImageView.glideImageWithParams(view: View, url: String) {
    Glide.with(view)
        .load(url)
        .into(this)
}

fun VideoView.setUrlAndMediaPlayer(context: Context, url: String) {
    val position = 0
    val mediaController = MediaController(context)
    setMediaController(mediaController)
    setVideoURI(Uri.parse(url))
    setOnPreparedListener {
        seekTo(position)

        if (position == 0) {
            start()
        } else {
            pause()
        }
    }
    start()
}