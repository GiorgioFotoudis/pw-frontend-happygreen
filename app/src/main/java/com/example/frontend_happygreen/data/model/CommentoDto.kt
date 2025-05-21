package com.example.frontend_happygreen.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CommentoDto(
    val id: Int,
    val post: Int,
    val autore: Int,
    val autore_username: String,
    val testo: String,
    val data_commento: String
)
