package com.example.reddit.extension

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide

fun ViewGroup.inflateLayout(@LayoutRes layoutId: Int, attachToParent: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToParent)
}

fun ImageView.glideImageWithParams(view: View, url: String, width: Int, height: Int) {
//    val curParams = layoutParams
//    curParams.width = width
//    curParams.height = height
//    layoutParams = curParams
    Glide.with(view)
        .load(url)
        .into(this)
}