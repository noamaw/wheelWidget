package com.example.spinwheel

import android.content.Context
import android.content.Intent
import com.example.spinwheel.ui.SpinWheelActivity

object SpinWheelLauncher {

    fun start(context: Context) {
        val intent = Intent(context, SpinWheelActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}