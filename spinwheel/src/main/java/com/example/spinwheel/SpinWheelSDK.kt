package com.example.spinwheel

import android.content.Context
import android.content.Intent
import com.example.spinwheel.ui.SpinWheelActivity

object SpinWheelSDK {

    fun start(context: Context, configUrl: String) {
        val intent = Intent(context, SpinWheelActivity::class.java)
        intent.putExtra("config_url", configUrl)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}