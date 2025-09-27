package com.example.lab_week_05.utils

import android.widget.ImageView

interface ImageLoader {
    fun loadImage(url: String, target: ImageView)
}
