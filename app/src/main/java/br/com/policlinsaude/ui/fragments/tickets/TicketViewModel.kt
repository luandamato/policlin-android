package br.com.policlinsaude.ui.fragments.tickets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.policlinsaude.data.local.SessionManager
import br.com.policlinsaude.data.models.TicketBodyModel
import br.com.policlinsaude.data.models.TicketDetail
import br.com.policlinsaude.data.repositories.AppRepository
import br.com.policlinsaude.util.helpers.SingleLiveEvent
import kotlinx.coroutines.launch

/** Eventos únicos da tela de 2ª via de boletos. */
sealed interface TicketEvent {
    data object Nothing : TicketEvent
    data class ShowError(val message: String) : TicketEvent
}

/**
 * ViewModel da tela de 2ª via de boletos.
 *
 * Migrado do legado `TicketViewModel` (stack antigo `com.policlinsaude.newfeature`),
 * que expunha `LiveData<ViewModelResponse<TicketModel, ServerErrorResponse>>`.
 * Convertido para o padrão do novo app: LiveData de estado (`loading`/`tickets`)
 * + [SingleLiveEvent] para erros, usando [AppRepository] e [SessionManager].
 *
 * Não referencia View/Activity/Fragment/Binding e não navega.
 */
class TicketViewModel(
    private val appRepository: AppRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    // Filtros selecionados (anos/mês) — persistidos no ViewModel para
    // reconstrução da tela (mesmo comportamento do legado).
    var yearSelected: String? = null
    var monthYearSelected: String? = null

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _tickets = MutableLiveData<MutableList<TicketDetail>?>(null)
    val tickets: LiveData<MutableList<TicketDetail>?> = _tickets

    private val _event = SingleLiveEvent<TicketEvent>()
    val event: LiveData<TicketEvent> = _event

    /**
     * Busca os boletos.
     * [option]: 1 = em aberto (default), 2 = por ano/mês, 3 = por ano.
     */
    fun onGetTickets(option: Int = 1, year: Int = 0, month: Int = 0) {
        val token = sessionManager.getToken()
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = appRepository.onGetTickets(
                    TicketBodyModel(token = token, option = option, year = year, month = month)
                )
                if (response.sdtBoleto.isNullOrEmpty()) {
                    _tickets.value = arrayListOf()
                    _event.value = TicketEvent.ShowError(
                        response.msgExterna.orEmpty().ifBlank { "Nenhum boleto encontrado." }
                    )
                } else {
                    _tickets.value = response.sdtBoleto
                }
            } catch (e: Exception) {
                _tickets.value = null
                _event.value = TicketEvent.ShowError(
                    e.message?.takeIf { it.isNotBlank() } ?: "Ocorreu um erro inesperado."
                )
            } finally {
                _loading.value = false
            }
        }
    }

    fun onClearSelected() {
        yearSelected = null
        monthYearSelected = null
    }
}