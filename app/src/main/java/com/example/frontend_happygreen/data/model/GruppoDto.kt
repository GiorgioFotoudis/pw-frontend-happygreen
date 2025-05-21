package com.example.frontend_happygreen.data.model

import kotlinx.serialization.Serializable

@Serializable
data class GruppoDto(
    val id: Int,
    val nome: String,
    val descrizione: String,
    val data_creazione: String,
    val creatore: Int,
    val creatore_username: String
)
