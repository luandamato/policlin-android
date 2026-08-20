package br.com.policlinsaude.ui.activities.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.policlinsaude.data.models.LoginRequest
import br.com.policlinsaude.data.services.ServiceLocator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

/**
 * LoginViewModel simplificado - sem @HiltViewModel
 * Injeção manual via ServiceLocator
 */
class LoginViewModel : ViewModel() {

    private val apiService = ServiceLocator.apiService()
    private val preferences = ServiceLocator.preferences()

    private val _loginStatus = MutableStateFlow<LoginStatus>(LoginStatus.Idle)
    val loginStatus: StateFlow<LoginStatus> = _loginStatus

    fun login(request: LoginRequest) {
        viewModelScope.launch {
            try {
                _loginStatus.value = LoginStatus.Loading

                val response = apiService.login(request).awaitResponse()

                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()!!
                    
                    // Salvar token
                    preferences.edit()
                        .putString("auth_token", loginResponse.token)
                        .apply()

                    _loginStatus.value = LoginStatus.Success
                } else {
                    _loginStatus.value = LoginStatus.Error(
                        response.message() ?: "Erro ao fazer login"
                    )
                }
            } catch (e: Exception) {
                _loginStatus.value = LoginStatus.Error(
                    e.message ?: "Erro de conexão"
                )
            }
        }
    }
}
