package com.example.colorsound.model

@kotlinx.serialization.Serializable
data class ResponseSounds(
    val status: String,
    val message: String,
    val data: List<Sound>,
)
