package com.example.spinwheel.models

import kotlinx.serialization.Serializable

@Serializable
data class WheelAssets(
    val bg: String,
    val wheelFrame: String,
    val wheelSpin: String,
    val wheel: String
)
