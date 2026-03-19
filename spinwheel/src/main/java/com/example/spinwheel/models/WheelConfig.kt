package com.example.spinwheel.models

import kotlinx.serialization.Serializable

@Serializable
data class WheelConfig(
    val rotation: RotationConfig,
    val assets: WheelAssets
)
