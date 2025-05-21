package com.example.frontend_happygreen.data.model

import kotlinx.serialization.Serializable

@Serializable
data class TokenDto(
    val access: String,
    val refresh: String
)
