package com.example.frontend_happygreen.data.model

import kotlinx.serialization.Serializable

@Serializable
data class QuizAnswerDto(
    val domanda_id: Int,
    val risposta_id: Int
)