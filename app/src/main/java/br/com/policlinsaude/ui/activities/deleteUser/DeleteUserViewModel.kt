package br.com.policlinsaude.ui.activities.deleteUser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.policlinsaude.data.local.SessionManager
import br.com.policlinsaude.data.models.DeleteUserRequest
import br.com.policlinsaude.data.repositories.AppRepository
import br.com.policlinsaude.util.helpers.SingleLiveEvent
import kotlinx.coroutines.launch

sealed interface DeleteUserEvent {
    data class ShowError(val message: String) : DeleteUserEvent
    data object DeleteSuccess : DeleteUserEvent
    data object LogoutSuccess : DeleteUserEvent
}

class DeleteUserViewModel(
    private val appRepository: AppRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _event = SingleLiveEvent<DeleteUserEvent>()
    val event: LiveData<DeleteUserEvent> = _event

    fun deleteUser() {
        val token = sessionManager.getToken()
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = appRepository.onDeleteUser(DeleteUserRequest(token = token))
                if (response.codAcao == 1) {
                    _event.value = DeleteUserEvent.DeleteSuccess
                } else {
                    _event.value = DeleteUserEvent.ShowError(response.msgExterna ?: "Erro ao excluir conta")
                }
            } catch (e: Exception) {
                _event.value = DeleteUserEvent.ShowError(e.message ?: "Erro inesperado")
            } finally {
                _loading.value = false
            }
        }
    }

    fun logout() {
        val token = sessionManager.getToken()
        viewModelScope.launch {
            _loading.value = true
            try {
                appRepository.onLogout(DeleteUserRequest(token = token))
                sessionManager.clearSession()
                _event.value = DeleteUserEvent.LogoutSuccess
            } catch (e: Exception) {
                // Mesmo com erro no logout, limpamos a sessão local
                sessionManager.clearSession()
                _event.value = DeleteUserEvent.LogoutSuccess
            } finally {
                _loading.value = false
            }
        }
    }
}
