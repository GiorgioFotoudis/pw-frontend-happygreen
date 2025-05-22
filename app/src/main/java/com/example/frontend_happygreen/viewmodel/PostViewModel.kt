package com.example.frontend_happygreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend_happygreen.data.model.PostDto
import com.example.frontend_happygreen.data.remote.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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
}