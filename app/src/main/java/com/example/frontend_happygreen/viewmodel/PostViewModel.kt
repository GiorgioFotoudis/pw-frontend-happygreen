package com.example.frontend_happygreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend_happygreen.data.model.PostDto
import com.example.frontend_happygreen.data.remote.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class PostViewModel : ViewModel() {
    private val api = ApiClient.service

    private val _posts = MutableStateFlow<List<PostDto>>(emptyList())
    val posts: StateFlow<List<PostDto>> get() = _posts

    fun loadPostsByGroup(gruppoId: Int, token: String) {
        viewModelScope.launch {
            runCatching {
                val allPosts = api.getPosts("Bearer $token")
                allPosts.filter { it.gruppo == gruppoId }
            }.onSuccess {
                _posts.value = it
            }.onFailure {
                _posts.value = emptyList()
            }
        }
    }

    /*fun creaPost(
        gruppoId: Int,
        descrizione: String,
        latitudine: Double,
        longitudine: Double,
        imageFile: File,
        riconoscimento: String,
        token: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val requestImage = imageFile
                    .asRequestBody("image/*".toMediaTypeOrNull())
                val imagePart = MultipartBody.Part
                    .createFormData("immagine", imageFile.name, requestImage)

                val descrizionePart = descrizione
                    .toRequestBody("text/plain".toMediaTypeOrNull())
                val latitudinePart = latitudine.toString()
                    .toRequestBody("text/plain".toMediaTypeOrNull())
                val longitudinePart = longitudine.toString()
                    .toRequestBody("text/plain".toMediaTypeOrNull())
                val riconoscimentoPart = riconoscimento.toString()
                    .toRequestBody("text/plain".toMediaTypeOrNull())

                api.creaPost(
                    token = "Bearer $token",
                    gruppo = gruppoId,
                    descrizione = descrizionePart,
                    latitudine = latitudinePart,
                    longitudine = longitudinePart,
                    immagine = imagePart,
                    riconoscimento = riconoscimentoPart
                )

                onSuccess()
            } catch (e: Exception) {
                onError(e.localizedMessage ?: "Errore durante l'invio del post.")
            }
        }
    }*/

     */
    fun creaPost(
        gruppoId: Int,
        descrizione: String,
        riconoscimento: String,
        latitudine: Double,
        longitudine: Double,
        token: String,
        imageFile: File?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val reqDescrizione = descrizione.toRequestBody("text/plain".toMediaTypeOrNull())
                val reqLat = latitudine.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                val reqLong = longitudine.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                val reqRiconoscimento = riconoscimento.toRequestBody("text/plain".toMediaTypeOrNull())

                val imagePart: MultipartBody.Part? = imageFile?.let {
                    val reqImage = it.asRequestBody("image/*".toMediaType())
                    MultipartBody.Part.createFormData("immagine", it.name, reqImage)
                }

                val post = api.creaPost(
                    token = "Bearer $token",
                    gruppo = gruppoId,
                    descrizione = reqDescrizione,
                    latitudine = reqLat,
                    longitudine = reqLong,
                    riconoscimento = reqRiconoscimento,
                    immagine = imagePart
                )
                onSuccess()
            } catch (e: Exception) {
                onError("Errore: ${e.message}")
            }
        }
    }
}

