package br.com.policlinsaude.ui.activities.editPassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.policlinsaude.data.local.SessionManager
import br.com.policlinsaude.data.repositories.AppRepository
import br.com.policlinsaude.util.helpers.SingleLiveEvent
import kotlinx.coroutines.launch

sealed interface EditPasswordEvent {
    data class ShowError(val message: String) : EditPasswordEvent
    data object ShowSuccess : EditPasswordEvent
}

class EditPasswordViewModel(
    private val appRepository: AppRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _event = SingleLiveEvent<EditPasswordEvent>()
    val event: LiveData<EditPasswordEvent> = _event

    fun onSendClicked(password: String) {
        val token = sessionManager.getToken()
        if (token.isEmpty()) return

        viewModelScope.launch {
            _loading.value = true
            try {
                val response = appRepository.onEditPassword(token, password)
                if (response.codAcao == 1) {
                    _event.value = EditPasswordEvent.ShowSuccess
                } else {
                    _event.value = EditPasswordEvent.ShowError(response.msgExterna ?: "Erro ao atualizar senha")
                }
            } catch (e: Exception) {
                _event.value = EditPasswordEvent.ShowError(e.message ?: "Erro inesperado")
            } finally {
                _loading.value = false
            }
        }
    }
}
