package com.example.spinwheel.repository

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import com.example.spinwheel.models.RootConfig
import com.example.spinwheel.models.WheelUiModel
import com.example.spinwheel.models.WidgetConfig
import com.example.spinwheel.remote.AssetDownloader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.IOException

class ConfigRepository(
    private val context: Context
) {

    private val client = OkHttpClient()
    private val assetDownloader = AssetDownloader(context, client)

    suspend fun getConfig(url: String): WidgetConfig {
        return withContext(Dispatchers.IO) {
            val cachedJson = getCachedJson(context)

            if (cachedJson != null) {
                val cachedRoot = Json.decodeFromString<RootConfig>(cachedJson)
                val cachedConfig = cachedRoot.data.first()

                val cacheExpiration = cachedConfig.network.attributes.cacheExpiration

                if (!shouldFetch(cacheExpiration)) {
                    Log.d("CONFIG", "Using cached config")
                    return@withContext cachedConfig
                }
            }

            try {
                Log.d("CONFIG", "Fetching from network")
                val json = fetchWithRetry(url, 3)
                cacheJson(context, json)
                saveFetchTime()

                val root = Json.decodeFromString<RootConfig>(json)
                return@withContext root.data.first()

            } catch (e: Exception) {
                if (cachedJson != null) {
                    Log.d("CONFIG", "Falling back to cache")
                    val root = Json.decodeFromString<RootConfig>(cachedJson)
                    return@withContext root.data.first()
                } else {
                    throw e
                }
            }
        }
    }

    private suspend fun fetchJson(url: String): String {
        return withContext(Dispatchers.IO) {
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()

            if (!response.isSuccessful) {
                throw IOException("Failed: $response")
            }

            response.body?.string() ?: throw IOException("Empty body")
        }
    }

    val prefs = context.getSharedPreferences("spinwheel", Context.MODE_PRIVATE)

    fun saveFetchTime() {
        prefs.edit().putLong("last_fetch", System.currentTimeMillis()).apply()
    }

    fun shouldFetch(cacheExpiration: Int): Boolean {
        val lastFetch = prefs.getLong("last_fetch", 0)
        val now = System.currentTimeMillis()
        return (now - lastFetch) > cacheExpiration * 1000
    }

    suspend fun fetchWithRetry(url: String, retries: Int): String {
        repeat(retries) { attempt ->
            try {
                return fetchJson(url)
            } catch (e: Exception) {
                if (attempt == retries - 1) throw e
            }
        }
        error("Unreachable")
    }


    suspend fun buildUiModel(config: WidgetConfig): WheelUiModel {
        val base = config.network.assets.host
        val bgFile = assetDownloader.downloadImage(base + config.wheel.assets.bg, "bg.png")
        val wheelFile = assetDownloader.downloadImage(base + config.wheel.assets.wheel, "wheel.png")
        val frameFile = assetDownloader.downloadImage(base + config.wheel.assets.wheelFrame, "frame.png")
        val spinFile = assetDownloader.downloadImage(base + config.wheel.assets.wheelSpin, "spin.png")

        return WheelUiModel(
            background = BitmapFactory.decodeFile(bgFile.absolutePath),
            wheel = BitmapFactory.decodeFile(wheelFile.absolutePath),
            frame = BitmapFactory.decodeFile(frameFile.absolutePath),
            spinButton = BitmapFactory.decodeFile(spinFile.absolutePath),
            rotationConfig = config.wheel.rotation
        )
    }

    fun cacheJson(context: Context, json: String) {
        val tempFile = File(context.filesDir, "config_temp.json")
        val finalFile = File(context.filesDir, "config.json")

        tempFile.writeText(json)
        tempFile.renameTo(finalFile)
    }

    fun getCachedJson(context: Context): String? {
        val file = File(context.filesDir, "config.json")
        return if (file.exists()) file.readText() else null
    }
}