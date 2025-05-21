package com.example.frontend_happygreen.data.model

import kotlinx.serialization.Serializable

@Serializable
data class QuizDto(
    val id: Int,
    val titolo: String,
    val descrizione: String,
    val domande: List<DomandaDto>
)
