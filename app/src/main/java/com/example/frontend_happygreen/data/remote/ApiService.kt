package com.example.frontend_happygreen.data.remote

import com.example.frontend_happygreen.data.model.*
import retrofit2.http.*
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface ApiService {

    // ---------- AUTENTICAZIONE ----------
    @POST("api/login/")
    suspend fun login(
        @Body body: LoginRequest
    ): TokenDto

    @POST("api/register/")
    suspend fun register(
        @Body body: RegisterRequest
    ): UserProfileDto

    @GET("api/profile/")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ): UserProfileDto

    // ---------- GRUPPI ----------
    @POST("gruppi/")
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
    suspend fun getAllPosts(
        @Header("Authorization") token: String
    ): List<PostDto>

    // Creazione post (richiede multipart)
    @Multipart
    @POST("api/posts/")
    suspend fun createPost(
        @Header("Authorization") token: String,
        @Part("gruppo") gruppo: RequestBody,
        @Part("riconoscimento") riconoscimento: RequestBody,
        @Part("descrizione") descrizione: RequestBody,
        @Part("latitudine") latitudine: RequestBody,
        @Part("longitudine") longitudine: RequestBody,
        @Part immagine: MultipartBody.Part
    ): PostDto

    // ---------- COMMENTI ----------
    @GET("api/posts/commenti/")
    suspend fun getAllComments(
        @Header("Authorization") token: String
    ): List<CommentoDto>

    @POST("api/posts/commenti/")
    suspend fun createComment(
        @Header("Authorization") token: String,
        @Body comment: CommentoDto // oppure crea un DTO dedicato per l'invio
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
