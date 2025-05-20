package com.example.frontend_happygreen.viewmodel

import androidx.lifecycle.ViewModel
import com.example.frontend_happygreen.data.model.*

class AuthViewModel : ViewModel() {
    var userProfile: UserProfileDto? = null
    var token: String? = null

    // login(), register(), getProfile()... (da fare)
}
