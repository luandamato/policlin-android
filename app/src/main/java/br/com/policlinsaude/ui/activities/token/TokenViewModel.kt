package br.com.policlinsaude.ui.activities.token

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.policlinsaude.data.local.SessionManager
import br.com.policlinsaude.data.models.BeneficiarioModel
import br.com.policlinsaude.data.models.BeneficiariosRequestModel
import br.com.policlinsaude.data.models.TokenBodyModel
import br.com.policlinsaude.data.models.TokenResponseModel
import br.com.policlinsaude.data.repositories.AppRepository
import br.com.policlinsaude.util.helpers.SingleLiveEvent
import kotlinx.coroutines.launch
import kotlin.collections.MutableList


sealed interface TokenEvent {
    data class SetDependents(val dependents: MutableList<BeneficiarioModel>) : TokenEvent
    data class SetUser(val user: BeneficiarioModel) : TokenEvent
    data class ShowError(val message: String) : TokenEvent
    data class ShowToken(val token: TokenResponseModel) : TokenEvent
}

class TokenViewModel(
    private val appRepository: AppRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    var ordem = ""
    var serviceToken = ""
    var validade = 2
    var dependents: MutableList<BeneficiarioModel> = arrayListOf()

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _event = SingleLiveEvent<TokenEvent>()
    val event: LiveData<TokenEvent> = _event

    fun getUsers() {
        val token = sessionManager.getToken()
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = appRepository.onGetDependents(BeneficiariosRequestModel(token))
                if (response.listaBeneficiario.isEmpty()) {
                    _event.value = TokenEvent.ShowError(
                        response.msgExterna.takeIf { it.isNotBlank() } ?: "Não foi possível carregar os beneficiarios."
                    )
                } else {
                    setUser(response.listaBeneficiario.first())
                    dependents = response.listaBeneficiario
                    _event.value = TokenEvent.SetDependents(response.listaBeneficiario)
                }
            } catch (e: Exception) {
                _event.value = TokenEvent.ShowError(
                    e.message?.takeIf { it.isNotBlank() } ?: "Ocorreu um erro inesperado."
                )
            } finally {
                _loading.value = false
            }
        }
    }

    fun getData() {
        val token = sessionManager.getToken()
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = appRepository.onGetToken(TokenBodyModel(token, ordem))
                if (response.tokenAtendimento?.isEmpty() ?: true) {
                    _event.value = TokenEvent.ShowError(
                        response.msgExterna.takeIf { it?.isNotBlank() ?: true } ?: "Não foi possível carregar os beneficiarios."
                    )
                } else {
                    serviceToken = response.tokenAtendimento
                    _event.value = TokenEvent.ShowToken(response)
                }
            } catch (e: Exception) {
                _event.value = TokenEvent.ShowError(
                    e.message?.takeIf { it.isNotBlank() } ?: "Ocorreu um erro inesperado."
                )
            } finally {
                _loading.value = false
            }
        }
    }

    fun setUser(user: BeneficiarioModel){
        this.ordem = user.ordem
        _event.value = TokenEvent.SetUser(user)
    }


}