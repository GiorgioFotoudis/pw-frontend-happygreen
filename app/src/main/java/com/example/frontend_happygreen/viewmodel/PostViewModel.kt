package com.example.frontend_happygreen.viewmodel

import androidx.lifecycle.ViewModel
import com.example.frontend_happygreen.data.model.PostDto

class PostViewModel : ViewModel() {
    var postList: List<PostDto> = emptyList()

    // getAllPosts(), createPost()... (da fare)
}
