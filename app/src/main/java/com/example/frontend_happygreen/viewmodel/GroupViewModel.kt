package com.example.frontend_happygreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend_happygreen.data.model.GruppoDto
import com.example.frontend_happygreen.data.remote.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GroupViewModel : ViewModel() {

    private val api = ApiClient.service

    private val _gruppi = MutableStateFlow<List<GruppoDto>>(emptyList())
    val gruppi: StateFlow<List<GruppoDto>> get() = _gruppi

    private val _mieiGruppi = MutableStateFlow<List<GruppoDto>>(emptyList())
    val mieiGruppi: StateFlow<List<GruppoDto>> get() = _mieiGruppi

    private val _iscrizioneSuccess = MutableStateFlow<Boolean?>(null)
    val iscrizioneSuccess: StateFlow<Boolean?> get() = _iscrizioneSuccess

    // Recupera tutti i gruppi disponibili
    fun loadAllGroups(token: String) {
        viewModelScope.launch {
            runCatching {
                api.getAllGroups("Bearer $token")
            }.onSuccess {
                _gruppi.value = it
            }.onFailure {
                _gruppi.value = emptyList()
            }
        }
    }

    // Recupera i gruppi dell'utente loggato
    fun loadMyGroups(token: String) {
        viewModelScope.launch {
            runCatching {
                api.getMyGroups("Bearer $token")
            }.onSuccess {
                _mieiGruppi.value = it
            }.onFailure {
                _mieiGruppi.value = emptyList()
            }
        }
    }

    // Richiede l'iscrizione a un gruppo
    fun iscrivitiAGruppo(gruppoId: Int, token: String) {
        viewModelScope.launch {
            runCatching {
                api.joinGroup(gruppoId, "Bearer $token")
            }.onSuccess {
                _iscrizioneSuccess.value = true
                loadMyGroups(token) // aggiorna lista dopo iscrizione
            }.onFailure {
                _iscrizioneSuccess.value = false
            }
        }
    }

    fun resetIscrizioneEsito() {
        _iscrizioneSuccess.value = null
    }
}
