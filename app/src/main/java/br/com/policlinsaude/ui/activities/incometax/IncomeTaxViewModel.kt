package br.com.policlinsaude.ui.activities.incometax

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.policlinsaude.data.local.SessionManager
import br.com.policlinsaude.data.models.IncomeTaxBodyModel
import br.com.policlinsaude.data.models.IncomeTaxItemModel
import br.com.policlinsaude.data.repositories.AppRepository
import br.com.policlinsaude.util.helpers.SingleLiveEvent
import kotlinx.coroutines.launch

/** Eventos únicos da tela de Imposto de Renda. */
sealed interface IncomeTaxEvent {
    data object Nothing : IncomeTaxEvent
    data class ShowError(val message: String) : IncomeTaxEvent
}

/**
 * ViewModel da tela de Imposto de Renda.
 *
 * Migrado do legado `IncomeTaxViewModel` (stack `com.policlinsaude.newfeature`),
 * que expunha `LiveData<ViewModelResponse<IncomeTaxResponseModel, ServerErrorResponse>>`.
 * Convertido para o padrão do novo app: LiveData de estado (`loading`/`incomeTax`)
 * + [SingleLiveEvent] para erros, usando [AppRepository] e [SessionManager].
 *
 * Não referencia View/Activity/Fragment/Binding e não navega.
 */
class IncomeTaxViewModel(
    private val appRepository: AppRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _incomeTax = MutableLiveData<MutableList<IncomeTaxItemModel>?>(null)
    val incomeTax: LiveData<MutableList<IncomeTaxItemModel>?> = _incomeTax

    private val _event = SingleLiveEvent<IncomeTaxEvent>()
    val event: LiveData<IncomeTaxEvent> = _event

    /** Busca a lista de informes de Imposto de Renda (endpoint `apiIR`). */
    fun onGetIncomeTax() {
        val token = sessionManager.getToken()
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = appRepository.onGetIncomeTax(IncomeTaxBodyModel(token = token))
                _incomeTax.value = response.listaIR
            } catch (e: Exception) {
                _incomeTax.value = null
                _event.value = IncomeTaxEvent.ShowError(
                    e.message?.takeIf { it.isNotBlank() } ?: "Ocorreu um erro inesperado."
                )
            } finally {
                _loading.value = false
            }
        }
    }
}