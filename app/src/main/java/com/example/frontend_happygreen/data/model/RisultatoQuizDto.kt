package com.example.frontend_happygreen.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RisultatoQuizDto(
    val id: Int,
    val utente: Int,
    val quiz: Int,
    val punteggio: Float,
    val data: String
)
