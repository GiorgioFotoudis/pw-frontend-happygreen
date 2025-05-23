package com.example.frontend_happygreen.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RisultatoQuizDto(
    val id: Int? = null,
    val utente: Int? = null,
    val quiz: Int,
    val quiz_titolo: String? = null,
    val punteggio: Float,
    val data: String? = null
)
