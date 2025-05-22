@kotlinx.serialization.Serializable
data class CommentoRichiesta(
    val post: Int,
    val testo: String,
    val autore: Int  // lo ricavi da authViewModel.userProfile?.id
)