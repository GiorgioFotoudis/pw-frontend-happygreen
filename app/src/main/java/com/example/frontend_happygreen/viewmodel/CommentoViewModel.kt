import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend_happygreen.data.model.CommentoDto
import com.example.frontend_happygreen.data.remote.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CommentViewModel : ViewModel() {
    private val api = ApiClient.service

    private val _commenti = MutableStateFlow<List<CommentoDto>>(emptyList())
    val commenti: StateFlow<List<CommentoDto>> get() = _commenti

    fun caricaCommenti(token: String) {
        viewModelScope.launch {
            runCatching {
                api.getCommenti("Bearer $token")
            }.onSuccess {
                _commenti.value = it
            }.onFailure {
                _commenti.value = emptyList()
            }
        }
    }

    fun aggiungiCommento(commento: CommentoRichiesta, token: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            runCatching {
                api.aggiungiCommento("Bearer $token", commento)
            }.onSuccess {
                onSuccess()
            }
        }
    }
}