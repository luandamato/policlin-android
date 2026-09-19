package br.com.policlinsaude.ui.activities.login

import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.policlinsaude.data.local.SessionManager
import br.com.policlinsaude.data.models.toPerson
import br.com.policlinsaude.data.repositories.AppRepository
import br.com.policlinsaude.util.helpers.SingleLiveEvent
import br.com.policlinsaude.utils.LogManager
import kotlinx.coroutines.launch

/**
 * Eventos únicos disparados pelo [LoginViewModel].
 */
sealed interface LoginEvent {
    data object NavigateToHome : LoginEvent
    data object NavigateToHomeWithoutLogin : LoginEvent
    data object NavigateToForgotPassword : LoginEvent
    data object NavigateToNotHasPassword : LoginEvent
    data class ShowError(val message: String) : LoginEvent
    data class ShowUpdateApp(val message: String) : LoginEvent
}

/**
 * ViewModel da tela de login.
 *
 * Fluxo (MVVM, sem Presenter/Navigator):
 * UI → ViewModel → AppRepository → AppService → API
 * UI ←── LiveData/Event ── ViewModel
 */
class LoginViewModel(
    private val appRepository: AppRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _event = SingleLiveEvent<LoginEvent>()
    val event: LiveData<LoginEvent> = _event

    /** Se já existe sessão salva, navega direto para a Home. */
    fun checkHasToken() {
        if (sessionManager.hasToken()) {
            _event.value = LoginEvent.NavigateToHome
        }
    }

    fun login(register: String, order: String, password: String, firebaseToken: String) {
        val osVersion = Build.VERSION.RELEASE
        LogManager.d(TAG, "login() chamado => register=$register order=$order password=$password firebaseToken=$firebaseToken osVersion=$osVersion")
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = appRepository.onLogin(
                    register = register,
                    order = order,
                    password = password,
                    firebaseToken = firebaseToken,
                    osVersion = osVersion
                )
                LogManager.d(TAG, "login() response => $response")

                val updateCodes = listOf(450, 455)
                val needUpdate = updateCodes.contains(response.actionCode ?: 0)
                val hasErrorMsg = response.msgInternal.isNullOrEmpty() || response.msgInternal != "OK"
                LogManager.d(TAG, "login() análise => actionCode=${response.actionCode} needUpdate=$needUpdate hasErrorMsg=$hasErrorMsg user=${response.user != null} token=${response.token != null}")

                if ((hasErrorMsg && !needUpdate) || response.user == null || response.token.isNullOrEmpty()) {
                    LogManager.d(TAG, "login() SEM SUCESSO => event=${
                        if (needUpdate) "ShowUpdateApp" else "ShowError"
                    } msgExternal=${response.msgExternal}")
                    _event.value = if (needUpdate) {
                        LoginEvent.ShowUpdateApp("${response.actionCode}-${response.msgExternal.orEmpty()}")
                    } else {
                        LoginEvent.ShowError(response.msgExternal.orEmpty())
                    }
                } else {
                    response.user?.let {
                        val person = it.toPerson()
                        LogManager.d(TAG, "login() salvando pessoa => $person")
                        sessionManager.savePerson(person)
                    }
                    response.token?.let {
                        LogManager.d(TAG, "login() salvando token => $it")
                        sessionManager.saveToken(it)
                    }
                    _event.value = LoginEvent.NavigateToHome
                }
            } catch (e: Exception) {
                LogManager.e(TAG, "login() EXCEPTION => ${e.message}", e)
                _event.value = LoginEvent.ShowError(
                    e.message?.takeIf { it.isNotBlank() } ?: "Ocorreu um erro inesperado"
                )
            } finally {
                _loading.value = false
            }
        }
    }

    private companion object {
        const val TAG = "LoginViewModel"
    }
}