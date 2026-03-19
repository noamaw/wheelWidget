package com.example.spinwheel.models

import kotlinx.serialization.Serializable

@Serializable
data class RootConfig(
    val data: List<WidgetConfig>,
    val meta: Meta
)
