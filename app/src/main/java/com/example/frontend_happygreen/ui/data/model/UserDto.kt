package com.example.frontend_happygreen.ui.data.model

@kotlinx.serialization.Serializable
data class UserDto(
    val id: Int,
    val username: String,
    val email: String,
    val is_superuser: Boolean,
    val is_staff: Boolean,
    val is_active: Boolean,
    val last_login: String?,
    val date_joined: String,
    val data_registrazione: String
)
