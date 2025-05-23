package com.example.frontend_happygreen.viewmodel

import android.util.Log
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

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    fun loadPostsByGroup(gruppoId: Int, token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            runCatching {
                Log.d("PostViewModel", "Caricando post per gruppo: $gruppoId")
                val allPosts = api.getPosts("Bearer $token")
                Log.d("PostViewModel", "Post totali ricevuti: ${allPosts.size}")
                val filteredPosts = allPosts.filter { it.gruppo == gruppoId }
                Log.d("PostViewModel", "Post filtrati per gruppo $gruppoId: ${filteredPosts.size}")
                filteredPosts
            }.onSuccess { filteredPosts ->
                _posts.value = filteredPosts
                Log.d("PostViewModel", "Post caricati con successo: ${filteredPosts.size}")
            }.onFailure { exception ->
                Log.e("PostViewModel", "Errore nel caricamento post: ${exception.message}", exception)
                _posts.value = emptyList()
            }.also {
                _isLoading.value = false
            }
        }
    }

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
                Log.d("PostViewModel", "Creando post per gruppo: $gruppoId")
                Log.d("PostViewModel", "Descrizione: $descrizione")
                Log.d("PostViewModel", "Immagine: ${imageFile?.name ?: "Nessuna"}")

                // CORREZIONE: gruppo deve essere RequestBody
                val reqGruppo = gruppoId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
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
                    gruppo = reqGruppo, // Ora è RequestBody
                    descrizione = reqDescrizione,
                    latitudine = reqLat,
                    longitudine = reqLong,
                    riconoscimento = reqRiconoscimento,
                    immagine = imagePart
                )

                Log.d("PostViewModel", "Post creato con successo: ${post.id}")

                // Ricarica i post per il gruppo corrente
                loadPostsByGroup(gruppoId, token)

                onSuccess()
            } catch (e: Exception) {
                Log.e("PostViewModel", "Errore nella creazione del post: ${e.message}", e)
                onError("Errore: ${e.message}")
            }
        }
    }

    fun refreshPosts(gruppoId: Int, token: String) {
        loadPostsByGroup(gruppoId, token)
    }
}