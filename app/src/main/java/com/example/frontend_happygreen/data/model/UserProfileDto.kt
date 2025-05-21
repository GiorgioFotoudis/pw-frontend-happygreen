package com.example.frontend_happygreen.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProfileDto(
    val id: Int,
    val username: String,
    val email: String,
    val data_registrazione: String? = null  // ← opzionale
)
