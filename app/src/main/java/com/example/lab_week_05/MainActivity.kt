package com.example.lab_week_05

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.lab_week_05.api.CatApiService
import com.example.lab_week_05.model.ImageData
import com.example.lab_week_05.utils.GlideLoader
import com.example.lab_week_05.utils.ImageLoader
import retrofit2.*
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {

    companion object {
        private const val BASE_URL = "https://api.thecatapi.com/v1/"
        private const val MAIN_ACTIVITY = "MainActivity"
    }

    // Retrofit instance
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    // Api Service
    private val catApiService by lazy {
        retrofit.create(CatApiService::class.java)
    }

    // View references
    private val apiResponseView: TextView by lazy {
        findViewById(R.id.api_response)
    }

    private val imageResultView: ImageView by lazy {
        findViewById(R.id.image_result)
    }

    private val refreshButton: Button by lazy {
        findViewById(R.id.refresh_button)
    }

    private val imageLoader: ImageLoader by lazy {
        GlideLoader(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Load pertama
        getCatImageResponse()

        // Kalau tombol refresh diklik
        refreshButton.setOnClickListener {
            apiResponseView.text = "Loading..."
            getCatImageResponse()
        }
    }

    private fun getCatImageResponse() {
        val call = catApiService.searchImages(1, "full", 1)
        call.enqueue(object : Callback<List<ImageData>> {
            override fun onFailure(call: Call<List<ImageData>>, t: Throwable) {
                Log.e(MAIN_ACTIVITY, "Failed to get response", t)
            }

            override fun onResponse(
                call: Call<List<ImageData>>,
                response: Response<List<ImageData>>
            ) {
                if (response.isSuccessful) {
                    val imageList = response.body()
                    val firstImage = imageList?.firstOrNull()

                    val imageUrl = firstImage?.imageUrl.orEmpty()
                    val breedName = firstImage?.breeds?.firstOrNull()?.name ?: "Unknown"

                    if (imageUrl.isNotBlank()) {
                        imageLoader.loadImage(imageUrl, imageResultView)
                    } else {
                        Log.d(MAIN_ACTIVITY, "Missing image URL")
                    }

                    apiResponseView.text = breedName
                } else {
                    Log.e(
                        MAIN_ACTIVITY,
                        "Failed to get response\n" + response.errorBody()?.string().orEmpty()
                    )
                }
            }
        })
    }
}
