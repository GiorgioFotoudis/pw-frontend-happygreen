package com.example.frontend_happygreen.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RispostaDto(
    val id: Int,
    val testo: String,
    val corretta: Boolean
)
