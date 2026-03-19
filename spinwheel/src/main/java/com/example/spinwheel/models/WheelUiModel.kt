package com.example.spinwheel.models

import android.graphics.Bitmap

data class WheelUiModel(
    val background: Bitmap,
    val wheel: Bitmap,
    val frame: Bitmap,
    val spinButton: Bitmap,
    val rotationConfig: RotationConfig
)
