package com.example.frontend_happygreen.data.model

import kotlinx.serialization.Serializable

@Serializable
data class QuizSubmissionDto(
    val risposte: List<QuizAnswerDto>
)