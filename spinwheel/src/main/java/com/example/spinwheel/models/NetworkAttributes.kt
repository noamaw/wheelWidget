package com.example.spinwheel.models

import kotlinx.serialization.Serializable

@Serializable
data class NetworkAttributes(
    val refreshInterval: Int,
    val networkTimeout: Int,
    val retryAttempts: Int,
    val cacheExpiration: Int,
    val debugMode: Boolean
)
