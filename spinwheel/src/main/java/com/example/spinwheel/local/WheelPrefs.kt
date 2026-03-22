package com.example.spinwheel.local

import android.content.Context

class WheelPrefs(context: Context) {

    private val prefs = context.getSharedPreferences("wheel_prefs", Context.MODE_PRIVATE)

    fun saveResult(index: Int) {
        prefs.edit().putInt("last_index", index).apply()
    }

    fun getResult(): Int {
        return prefs.getInt("last_index", -1)
    }

    fun saveLastFetch(time: Long) {
        prefs.edit().putLong("last_fetch", time).apply()
    }

    fun getLastFetch(): Long {
        return prefs.getLong("last_fetch", 0)
    }
}