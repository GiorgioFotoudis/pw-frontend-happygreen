package com.example.frontend_happygreen.ui.data.model

@kotlinx.serialization.Serializable
data class GruppoDto(
    val id: Int,
    val nome: String,
    val descrizione: String,
    val data_creazione: String,
    val creatore: Int,
    val creatore_username: String
)
