package com.example.frontend_happygreen.data.model

import kotlinx.serialization.Serializable

@Serializable
data class BadgeDto(
    val id: Int,
    val nome: String,
    val descrizione: String,
    val punti_assegnati: Int
)
