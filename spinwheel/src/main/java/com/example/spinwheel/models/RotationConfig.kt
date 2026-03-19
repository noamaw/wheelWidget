package com.example.spinwheel.models

import kotlinx.serialization.Serializable

@Serializable
data class RotationConfig(
    val duration: Int,
    val minimumSpins: Int,
    val maximumSpins: Int,
    val spinEasing: String
)
