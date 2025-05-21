package com.example.frontend_happygreen.data.model

import kotlinx.serialization.Serializable

@Serializable
data class DomandaDto(
    val id: Int,
    val testo: String,
    val risposte: List<RispostaDto>
)
