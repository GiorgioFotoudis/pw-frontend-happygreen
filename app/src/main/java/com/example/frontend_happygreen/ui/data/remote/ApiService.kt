package com.example.frontend_happygreen.ui.data.remote

import com.example.frontend_happygreen.ui.data.model.*
import retrofit2.http.*

interface ApiService {

    // ---------- AUTENTICAZIONE ----------
    @POST("api/login/")
    suspend fun login(@Body body: LoginRequest): TokenDto

    @POST("api/register/")
    suspend fun register(@Body body: RegisterRequest): UserProfileDto

    @GET("api/profile/")
    suspend fun getProfile(@Header("Authorization") token: String): UserProfileDto

    // ---------- GRUPPI ----------
    @GET("api/gruppi/")
    suspend fun getAllGroups(
        @Header("Authorization") token: String
    ): List<GruppoDto>

    @GET("api/gruppi/miei_gruppi/")
    suspend fun getMyGroups(
        @Header("Authorization") token: String
    ): List<GruppoDto>

    @POST("api/gruppi/{id}/iscriviti/")
    suspend fun joinGroup(
        @Path("id") groupId: Int,
        @Header("Authorization") token: String
    ): UtenteGruppoDto
}
