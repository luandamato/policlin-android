package br.com.policlinsaude.ui.activities.editPhone

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.policlinsaude.data.local.SessionManager
import br.com.policlinsaude.data.repositories.AppRepository
import br.com.policlinsaude.util.helpers.SingleLiveEvent
import kotlinx.coroutines.launch

sealed interface EditPhoneEvent {
    data class ShowError(val message: String) : EditPhoneEvent
    data object ShowSuccess : EditPhoneEvent
}

class EditPhoneViewModel(
    private val appRepository: AppRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _event = SingleLiveEvent<EditPhoneEvent>()
    val event: LiveData<EditPhoneEvent> = _event

    fun onSendClicked(phone: String) {
        if (phone.length < 10) {
            _event.value = EditPhoneEvent.ShowError("Telefone inválido")
            return
        }

        val token = sessionManager.getToken()
        val codeArea = phone.substring(0, 2)
        val phoneNumber = phone.substring(2)

        viewModelScope.launch {
            _loading.value = true
            try {
                val response = appRepository.onEditPhone(token, codeArea, phoneNumber)
                if (response.codAcao == 1) {
                    _event.value = EditPhoneEvent.ShowSuccess
                } else {
                    _event.value = EditPhoneEvent.ShowError(response.msgExterna ?: "Erro ao atualizar telefone")
                }
            } catch (e: Exception) {
                _event.value = EditPhoneEvent.ShowError(e.message ?: "Erro inesperado")
            } finally {
                _loading.value = false
            }
        }
    }
}
