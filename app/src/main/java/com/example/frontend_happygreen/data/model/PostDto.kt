package com.example.frontend_happygreen.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PostDto(
    val id: Int,
    val gruppo: Int,
    val gruppo_nome: String? = null, // Nullable nel caso non sia sempre presente
    val autore: Int,
    val autore_username: String? = null, // Nullable nel caso non sia sempre presente
    val immagine: String? = null, // IMPORTANTE: Nullable perché può essere null quando non c'è immagine
    val riconoscimento: String? = null, // Nullable
    val descrizione: String,
    val latitudine: Float? = null, // Nullable
    val longitudine: Float? = null, // Nullable
    val data_pubblicazione: String? = null // Nullable
)