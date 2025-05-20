package com.example.frontend_happygreen.ui.data.model

@kotlinx.serialization.Serializable
data class UtenteGruppoDto(
    val id: Int,
    val utente: Int,
    val gruppo: Int,
    val data_iscrizione: String
)