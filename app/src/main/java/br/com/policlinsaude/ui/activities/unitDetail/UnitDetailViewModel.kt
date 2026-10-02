package br.com.policlinsaude.ui.activities.unitDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.policlinsaude.data.local.SessionManager
import br.com.policlinsaude.data.models.JsonMedicalGuidePlanResponse
import br.com.policlinsaude.data.models.PresentationEstablishment
import br.com.policlinsaude.data.repositories.AppRepository
import br.com.policlinsaude.util.helpers.SingleLiveEvent
import com.google.gson.Gson
import kotlinx.coroutines.launch

/**
 * Eventos únicos de la pantalla de detalle de la unidad.
 */
sealed interface UnitDetailEvent {
    data class FavoriteChanged(val favorited: Boolean) : UnitDetailEvent
    data class ShowError(val message: String) : UnitDetailEvent
    data object ShowLogin : UnitDetailEvent
    data class ShowPlans(val plans: List<JsonMedicalGuidePlanResponse>) : UnitDetailEvent
    data object ShowEmptyPlans : UnitDetailEvent
}

/**
 * ViewModel da tela de detalhe da unidade.
 *
 * - Como o [PresentationEstablishment] não usa Parcelize (premissa do projeto),
 *   o estabelecimento é recebido via Intent como JSON string e convertido aqui.
 * - Para o modo Rede Propia (caller != "Units") coordina favoritos
 *   (MAPP_ManutencaoFavoritos: INSERIR/DELETAR) e planos
 *   (MAPP_RetornaGuiaMedicoDetalhes), mesma lógica do legado
 *   `MedicalGuideDetailsPresenterImpl`.
 *
 * Não referencia View/Activity/Fragment/Binding e não navega.
 */
class UnitDetailViewModel(
    private val appRepository: AppRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val gson = Gson()

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _event = SingleLiveEvent<UnitDetailEvent>()
    val event: LiveData<UnitDetailEvent> = _event

    fun parseEstablishment(json: String?): PresentationEstablishment? {
        if (json.isNullOrBlank()) return null
        return try {
            gson.fromJson(json, PresentationEstablishment::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun toJson(establishment: PresentationEstablishment): String = gson.toJson(establishment)

    /**
     * Alterna favorito do estabelecimento (legado: onFavoriteClicked).
     * Token vazio → evento [UnitDetailEvent.ShowLogin] para que a UI ofereça o login.
     */
    fun toggleFavorite(establishment: PresentationEstablishment) {
        val token = sessionManager.getToken()
        if (token.isEmpty()) {
            _event.value = UnitDetailEvent.ShowLogin
            return
        }
        viewModelScope.launch {
            _loading.value = true
            try {
                if (establishment.favorited) {
                    appRepository.onRemoveFromFavorites(
                        token,
                        METHOD_REMOVE,
                        establishment.proCls,
                        establishment.proCod,
                        establishment.proUf,
                        establishment.prsCod,
                        establishment.prsSeq,
                        establishment.esCod,
                        establishment.uType
                    )
                    establishment.favorited = false
                } else {
                    appRepository.onAddToFavorite(
                        token,
                        METHOD_ADD,
                        establishment.proCls,
                        establishment.proCod,
                        establishment.proUf,
                        establishment.prsCod,
                        establishment.prsSeq,
                        establishment.esCod,
                        establishment.uType
                    )
                    establishment.favorited = true
                }
                _event.value = UnitDetailEvent.FavoriteChanged(establishment.favorited)
            } catch (e: Exception) {
                _event.value = UnitDetailEvent.ShowError(e.message ?: "")
            } finally {
                _loading.value = false
            }
        }
    }

    /** Carga los planos (legado: onPlansClicked). */
    fun loadPlans(establishment: PresentationEstablishment) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = appRepository.onGetMedicalGuideDetails(
                    establishment.proUf,
                    establishment.prsCod,
                    establishment.proCls,
                    establishment.proCod
                )
                val plans = response.plans ?: emptyList()
                _event.value = if (plans.isEmpty()) {
                    UnitDetailEvent.ShowEmptyPlans
                } else {
                    UnitDetailEvent.ShowPlans(plans)
                }
            } catch (e: Exception) {
                _event.value = UnitDetailEvent.ShowError(e.message ?: "")
            } finally {
                _loading.value = false
            }
        }
    }

    private companion object {
        const val METHOD_ADD = "INSERIR"
        const val METHOD_REMOVE = "DELETAR"
    }
}