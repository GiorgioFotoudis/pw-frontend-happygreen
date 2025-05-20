package com.example.frontend_happygreen.data.model

import kotlinx.serialization.Serializable

@Serializable
data class IscrizioneGruppoRequest(
    val gruppo: Int,
    val utente: Int
)
