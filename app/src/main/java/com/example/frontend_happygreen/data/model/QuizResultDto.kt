package com.example.frontend_happygreen.data.model

import kotlinx.serialization.Serializable

@Serializable
data class QuizResultDto(
    val punteggio: Float,
    val risposte_corrette: Int,
    val total_domande: Int,
    val risultato_id: Int
)