package com.example.lab_week_05.model

import com.squareup.moshi.Json

data class ImageData(
    val id: String?,
    @Json(name = "url") val url: String,
    val breeds: List<CatBreedData>?,
    val width: Int?,
    val height: Int?
)
