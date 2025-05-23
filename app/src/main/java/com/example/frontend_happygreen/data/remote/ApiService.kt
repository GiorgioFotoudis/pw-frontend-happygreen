package com.example.frontend_happygreen.data.remote

import CommentoRichiesta
import com.example.frontend_happygreen.data.model.*
import retrofit2.http.*
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface ApiService {

    // ---------- AUTENTICAZIONE ----------
    @POST("api/auth/login/")
    suspend fun login(
        @Body body: LoginRequest
    ): TokenDto

    @POST("api/auth/register/")
    suspend fun register(
        @Body body: RegisterRequest
    ): UserProfileDto

    @GET("api/auth/profile/")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ): UserProfileDto

    // ---------- GRUPPI ----------
    @POST("api/gruppi/")
    suspend fun createGroup(
        @Header("Authorization") token: String,
        @Body body: Map<String, String>
    ): GruppoDto

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

    // ---------- POST ----------
    @GET("api/posts/")
    suspend fun getPosts(
        @Header("Authorization") token: String
    ): List<PostDto>

    // CORREZIONE: gruppo deve essere RequestBody, non Int diretto
    @Multipart
    @POST("api/posts/")
    suspend fun creaPost(
        @Header("Authorization") token: String,
        @Part("gruppo") gruppo: RequestBody, // Cambiato da Int a RequestBody
        @Part("descrizione") descrizione: RequestBody,
        @Part("latitudine") latitudine: RequestBody,
        @Part("longitudine") longitudine: RequestBody,
        @Part("riconoscimento") riconoscimento: RequestBody,
        @Part immagine: MultipartBody.Part?
    ): PostDto

    // ---------- COMMENTI ----------
    @GET("api/commenti/")
    suspend fun getCommenti(
        @Header("Authorization") token: String
    ): List<CommentoDto>

    @POST("api/commenti/")
    suspend fun aggiungiCommento(
        @Header("Authorization") token: String,
        @Body comment: CommentoRichiesta
    ): CommentoDto

    // ---------- QUIZ ----------
    @GET("api/quiz/")
    suspend fun getAllQuiz(
        @Header("Authorization") token: String
    ): List<QuizDto>

    @GET("api/quiz/risultati/")
    suspend fun getUserQuizResults(
        @Header("Authorization") token: String
    ): List<RisultatoQuizDto>

    @POST("api/quiz/risultati/")
    suspend fun submitQuizResult(
        @Header("Authorization") token: String,
        @Body risultato: RisultatoQuizDto
    ): RisultatoQuizDto

    // ---------- PRODOTTI ----------
    @GET("api/prodotti/")
    suspend fun getAllProducts(
        @Header("Authorization") token: String
    ): List<ProdottoDto>

    @GET("api/prodotti/miei_prodotti/")
    suspend fun getProdottiScansionati(
        @Header("Authorization") token: String
    ): List<ProdottoDto>

    @POST("api/prodotti/{id}/scansiona/")
    suspend fun scansionaProdotto(
        @Path("id") prodottoId: Int,
        @Header("Authorization") token: String
    ): ProdottoScansionatoDto

    // ---------- BADGES ----------
    @GET("api/badges/")
    suspend fun getAllBadges(
        @Header("Authorization") token: String
    ): List<BadgeDto>

    @GET("api/badges/utenti/")
    suspend fun getUserBadges(
        @Header("Authorization") token: String
    ): List<UtenteBadgeDto>
}