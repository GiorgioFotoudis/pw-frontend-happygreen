package com.example.frontend_happygreen.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PostDto(
    val id: Int,
    val gruppo: Int,
    val gruppo_nome: String,
    val autore: Int,
    val autore_username: String,
    val immagine: String,
    val riconoscimento: String,
    val descrizione: String,
    val latitudine: Float,
    val longitudine: Float,
    val data_pubblicazione: String
)
