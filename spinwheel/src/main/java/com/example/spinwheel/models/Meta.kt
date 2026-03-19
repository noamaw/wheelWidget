package com.example.spinwheel.models

import kotlinx.serialization.Serializable

@Serializable
data class Meta(
    val version: Int,
    val copyright: String
)
