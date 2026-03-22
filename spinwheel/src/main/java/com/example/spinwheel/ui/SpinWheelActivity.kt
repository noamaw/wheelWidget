package com.example.spinwheel.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.spinwheel.local.WheelPrefs
import com.example.spinwheel.repository.ConfigRepository
import androidx.activity.compose.setContent
import com.example.spinwheel.utils.DEFAULT_URL

class SpinWheelActivity : ComponentActivity() {

    private lateinit var repository: ConfigRepository
    private lateinit var prefs: WheelPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val url = intent.getStringExtra("config_url") ?: DEFAULT_URL
        repository = ConfigRepository(applicationContext)
        prefs = WheelPrefs(applicationContext)

        setContent {
            SpinWheelScreen(repository, prefs)
        }
    }
}
