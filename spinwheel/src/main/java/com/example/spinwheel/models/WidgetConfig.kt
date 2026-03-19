package com.example.spinwheel.models

import kotlinx.serialization.Serializable

@Serializable
data class WidgetConfig(
    val id: String,
    val name: String,
    val type: String,
    val network: NetworkConfig,
    val wheel: WheelConfig
)
