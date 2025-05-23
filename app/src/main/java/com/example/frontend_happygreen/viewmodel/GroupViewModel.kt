package com.example.frontend_happygreen.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend_happygreen.data.model.GruppoDto
import com.example.frontend_happygreen.data.remote.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GroupViewModel : ViewModel() {

    private val api = ApiClient.service
    private val TAG = "GroupViewModel"

    private val _gruppi = MutableStateFlow<List<GruppoDto>>(emptyList())
    val gruppi: StateFlow<List<GruppoDto>> get() = _gruppi

    private val _mieiGruppi = MutableStateFlow<List<GruppoDto>>(emptyList())
    val mieiGruppi: StateFlow<List<GruppoDto>> get() = _mieiGruppi

    private val _iscrizioneSuccess = MutableStateFlow<Boolean?>(null)
    val iscrizioneSuccess: StateFlow<Boolean?> get() = _iscrizioneSuccess

    fun creaGruppo(nome: String, descrizione: String, token: String, onResult: (Boolean) -> Unit) {
        Log.d(TAG, "creaGruppo chiamato - nome: $nome, descrizione: $descrizione")
        viewModelScope.launch {
            runCatching {
                val body = mapOf(
                    "nome" to nome,
                    "descrizione" to descrizione
                )
                Log.d(TAG, "Creazione gruppo - body: $body")
                val result = api.createGroup("Bearer $token", body)
                Log.d(TAG, "Gruppo creato con successo: $result")
                result
            }.onSuccess {
                Log.d(TAG, "Gruppo creato con successo, ricarico lista gruppi")
                loadAllGroups(token)
                onResult(true)
            }.onFailure { exception ->
                Log.e(TAG, "Errore nella creazione del gruppo", exception)
                onResult(false)
            }
        }
    }

    // Recupera tutti i gruppi disponibili
    fun loadAllGroups(token: String) {
        Log.d(TAG, "loadAllGroups chiamato")
        viewModelScope.launch {
            runCatching {
                Log.d(TAG, "Chiamata API getAllGroups con token: Bearer ${token.take(10)}...")
                val result = api.getAllGroups("Bearer $token")
                Log.d(TAG, "Risposta API getAllGroups ricevuta: ${result.size} gruppi")
                result.forEach { gruppo ->
                    Log.d(TAG, "Gruppo ricevuto: ID=${gruppo.id}, Nome=${gruppo.nome}, Creatore=${gruppo.creatore_username}")
                }
                result
            }.onSuccess { gruppiList ->
                Log.d(TAG, "Aggiornamento _gruppi.value con ${gruppiList.size} gruppi")
                _gruppi.value = gruppiList
                Log.d(TAG, "Stato _gruppi aggiornato. Valore attuale: ${_gruppi.value.size} gruppi")
            }.onFailure { exception ->
                Log.e(TAG, "Errore nel caricamento di tutti i gruppi", exception)
                Log.e(TAG, "Tipo errore: ${exception.javaClass.simpleName}")
                Log.e(TAG, "Messaggio errore: ${exception.message}")
                _gruppi.value = emptyList()
            }
        }
    }

    // Recupera i gruppi dell'utente loggato
    fun loadMyGroups(token: String) {
        Log.d(TAG, "loadMyGroups chiamato")
        viewModelScope.launch {
            runCatching {
                Log.d(TAG, "Chiamata API getMyGroups con token: Bearer ${token.take(10)}...")
                val result = api.getMyGroups("Bearer $token")
                Log.d(TAG, "Risposta API getMyGroups ricevuta: ${result.size} gruppi")
                result.forEach { gruppo ->
                    Log.d(TAG, "Mio gruppo: ID=${gruppo.id}, Nome=${gruppo.nome}")
                }
                result
            }.onSuccess { mieiGruppiList ->
                Log.d(TAG, "Aggiornamento _mieiGruppi.value con ${mieiGruppiList.size} gruppi")
                _mieiGruppi.value = mieiGruppiList
                Log.d(TAG, "Stato _mieiGruppi aggiornato. Valore attuale: ${_mieiGruppi.value.size} gruppi")
            }.onFailure { exception ->
                Log.e(TAG, "Errore nel caricamento dei miei gruppi", exception)
                Log.e(TAG, "Tipo errore: ${exception.javaClass.simpleName}")
                Log.e(TAG, "Messaggio errore: ${exception.message}")
                _mieiGruppi.value = emptyList()
            }
        }
    }

    // Richiede l'iscrizione a un gruppo
    fun iscrivitiAGruppo(gruppoId: Int, token: String) {
        Log.d(TAG, "iscrivitiAGruppo chiamato per gruppo ID: $gruppoId")
        viewModelScope.launch {
            runCatching {
                Log.d(TAG, "Chiamata API joinGroup per gruppo $gruppoId")
                val result = api.joinGroup(gruppoId, "Bearer $token")
                Log.d(TAG, "Iscrizione completata con successo: $result")
                result
            }.onSuccess {
                Log.d(TAG, "Iscrizione riuscita, aggiorno stato e ricarico i miei gruppi")
                _iscrizioneSuccess.value = true
                loadMyGroups(token) // aggiorna lista dopo iscrizione
            }.onFailure { exception ->
                Log.e(TAG, "Errore nell'iscrizione al gruppo", exception)
                Log.e(TAG, "Tipo errore: ${exception.javaClass.simpleName}")
                Log.e(TAG, "Messaggio errore: ${exception.message}")
                _iscrizioneSuccess.value = false
            }
        }
    }

    fun resetIscrizioneEsito() {
        Log.d(TAG, "resetIscrizioneEsito chiamato")
        _iscrizioneSuccess.value = null
    }

    // Funzione di debug per controllare lo stato corrente
    fun debugCurrentState() {
        Log.d(TAG, "=== DEBUG STATE ===")
        Log.d(TAG, "Tutti i gruppi (${_gruppi.value.size}):")
        _gruppi.value.forEach { gruppo ->
            Log.d(TAG, "  - ${gruppo.nome} (ID: ${gruppo.id})")
        }
        Log.d(TAG, "I miei gruppi (${_mieiGruppi.value.size}):")
        _mieiGruppi.value.forEach { gruppo ->
            Log.d(TAG, "  - ${gruppo.nome} (ID: ${gruppo.id})")
        }
        Log.d(TAG, "Iscrizione success: ${_iscrizioneSuccess.value}")
        Log.d(TAG, "==================")
    }
}