package com.example.frontend_happygreen.ui.data.model

@kotlinx.serialization.Serializable
data class TokenDto(
    val access: String,
    val refresh: String
)
