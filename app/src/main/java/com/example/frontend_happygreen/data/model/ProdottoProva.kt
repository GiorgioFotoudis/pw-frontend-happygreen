package com.example.frontend_happygreen.data.model

data class ProdottoFake(
    val barcode: String,
    val nome: String,
    val materiali: String,
    val ecoRating: Int,
    val descrizione: String,
    val alternative: List<String>
)

val prodottiFinti = listOf(
    ProdottoFake(
        barcode = "1234567890123",
        nome = "Bottiglia in plastica",
        materiali = "Plastica PET",
        ecoRating = 2,
        descrizione = "Non riciclata. Impatto ambientale medio.",
        alternative = listOf("Bottiglia in vetro", "Bottiglia biodegradabile")
    ),
    ProdottoFake(
        barcode = "9876543210987",
        nome = "Cartone riciclato",
        materiali = "Carta riciclata",
        ecoRating = 5,
        descrizione = "Completamente eco-friendly.",
        alternative = listOf("Carta da fibre vegetali", "Imballaggio compostabile")
    )
)
