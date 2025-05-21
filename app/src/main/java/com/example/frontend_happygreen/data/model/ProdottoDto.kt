package com.example.frontend_happygreen.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ProdottoDto(
    val id: Int,
    val nome: String,
    val barcode: String,
    val eco_rating: Int,
    val materiali: String,
    val descrizione: String
)
