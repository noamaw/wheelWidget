package com.example.spinwheel.react

import com.example.spinwheel.SpinWheelLauncher
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

class SpinWheelModule(
    private val reactContext: ReactApplicationContext
) : ReactContextBaseJavaModule(reactContext) {

    override fun getName() = "SpinWheelModule"

    @ReactMethod
    fun openWheel() {
        SpinWheelLauncher.start(reactContext)
    }
}