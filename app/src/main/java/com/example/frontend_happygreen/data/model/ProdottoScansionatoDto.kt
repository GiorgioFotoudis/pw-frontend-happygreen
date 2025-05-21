package com.example.frontend_happygreen.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ProdottoScansionatoDto(
    val id: Int,
    val utente: Int,
    val prodotto: Int,
    val data_scansione: String
)
