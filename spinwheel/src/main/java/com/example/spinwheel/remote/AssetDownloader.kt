package com.example.spinwheel.remote

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File

class AssetDownloader(
    private val context: Context,
    private val client: OkHttpClient
) {
    suspend fun downloadImage(url: String, fileName: String): File {
        return withContext(Dispatchers.IO) {
            val file = File(context.filesDir, fileName)
            if (file.exists()) {
                return@withContext file
            }

            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()

            if (!response.isSuccessful) {
                throw okio.IOException("Failed to download image")
            }
            Log.d("CONTENT_TYPE", response.header("Content-Type") ?: "none")

            response.body?.byteStream()?.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            file
        }
    }

}