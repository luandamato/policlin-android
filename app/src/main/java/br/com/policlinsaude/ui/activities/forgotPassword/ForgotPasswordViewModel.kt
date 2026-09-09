package br.com.policlinsaude.ui.activities.forgotPassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.policlinsaude.data.repositories.AppRepository
import br.com.policlinsaude.util.helpers.SingleLiveEvent
import kotlinx.coroutines.launch

sealed interface ForgotPasswordEvent {
    data object ShowSuccess : ForgotPasswordEvent
    data class ShowError(val message: String) : ForgotPasswordEvent
    data object Finish : ForgotPasswordEvent
}

class ForgotPasswordViewModel(
    private val appRepository: AppRepository
) : ViewModel() {

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _event = SingleLiveEvent<ForgotPasswordEvent>()
    val event: LiveData<ForgotPasswordEvent> = _event

    fun recoverPassword(register: String, order: String, email: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = appRepository.onRecoverPassword(register, order, email)
                val isSuccess = response.msgInternal.equals("OK", ignoreCase = true)

                if (isSuccess) {
                    _event.value = ForgotPasswordEvent.ShowSuccess
                } else {
                    _event.value = ForgotPasswordEvent.ShowError(
                        response.msgExternal?.takeIf { it.isNotBlank() }
                            ?: "Não foi possível recuperar a senha."
                    )
                }
            } catch (e: Exception) {
                _event.value = ForgotPasswordEvent.ShowError(
                    e.message?.takeIf { it.isNotBlank() } ?: "Ocorreu um erro inesperado"
                )
            } finally {
                _loading.value = false
            }
        }
    }

    fun onBackClicked() {
        _event.value = ForgotPasswordEvent.Finish
    }

    fun onSuccessDialogOkClicked() {
        _event.value = ForgotPasswordEvent.Finish
    }
}
