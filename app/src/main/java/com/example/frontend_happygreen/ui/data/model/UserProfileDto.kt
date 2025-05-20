package com.example.frontend_happygreen.ui.data.model

@kotlinx.serialization.Serializable
data class UserProfileDto(
    val id: Int,
    val username: String,
    val email: String,
    val data_registrazione: String
)
