package com.example.spinwheel.models

import kotlinx.serialization.Serializable

@Serializable
data class NetworkConfig(
    val attributes: NetworkAttributes,
    val assets: NetworkAssets
)
