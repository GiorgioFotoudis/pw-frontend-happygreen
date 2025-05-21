package com.example.frontend_happygreen.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UtenteGruppoDto(
    val id: Int,
    val utente: Int,
    val gruppo: Int,
    val data_iscrizione: String
)