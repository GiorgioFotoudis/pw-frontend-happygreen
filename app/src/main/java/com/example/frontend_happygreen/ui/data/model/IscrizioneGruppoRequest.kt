package com.example.frontend_happygreen.ui.data.model

@kotlinx.serialization.Serializable
data class IscrizioneGruppoRequest(
    val gruppo: Int,
    val utente: Int
)
