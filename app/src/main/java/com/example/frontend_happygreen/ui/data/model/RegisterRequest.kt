package com.example.frontend_happygreen.ui.data.model

@kotlinx.serialization.Serializable
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val password2: String
)
