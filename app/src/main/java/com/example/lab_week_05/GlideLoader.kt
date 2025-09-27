package com.example.lab_week_05.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide

class GlideLoader(private val context: Context) : ImageLoader {
    override fun loadImage(url: String, target: ImageView) {
        Glide.with(context)
            .load(url)
            .centerCrop()
            .into(target)
    }
}
