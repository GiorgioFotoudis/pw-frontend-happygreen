package com.example.frontend_happygreen.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UtenteBadgeDto(
    val id: Int,
    val utente: Int,
    val badge: Int,
    val badge_nome: String,
    val badge_descrizione: String,
    val punti_assegnati: Int,
    val data_conseguimento: String
)
