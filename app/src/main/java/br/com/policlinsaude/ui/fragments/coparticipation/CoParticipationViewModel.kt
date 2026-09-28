package br.com.policlinsaude.ui.fragments.coparticipation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.policlinsaude.data.local.SessionManager
import br.com.policlinsaude.data.models.CoParticipationBodyCombo
import br.com.policlinsaude.data.models.CoParticipationBodyValue
import br.com.policlinsaude.data.models.CoParticipationItems
import br.com.policlinsaude.data.models.CoParticipationModel
import br.com.policlinsaude.data.repositories.AppRepository
import br.com.policlinsaude.util.helpers.SingleLiveEvent
import br.com.policlinsaude.utils.LogManager
import kotlinx.coroutines.launch

/**
 * ViewModel da Pesquisa de Valores de Coparticipação.
 *
 * Migrado de `_legacy/.../otherFeatures/features/coparticipation/ui/viewmodels/CoParticipationViewModel.kt`
 * (stack `com.policlinsaude.newfeature`, que expunha `ViewModelResponse<...>`); agora usando
 * `AppRepository` (endpoints `APIValoresCopartCombos` / `apiValoresCopart`) e `SessionManager`
 * para o token, com LiveData/`SingleLiveEvent` no padrão do novo app.
 */
class CoParticipationViewModel(
    private val appRepository: AppRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    // =====================================================================
    // Estado da tela (seleções e itens dos combos — preservado do legado)
    // =====================================================================
    var itemsComboOne: MutableList<String>? = null
    var itemsComboTwo: MutableList<String>? = null
    var itemsComboThree: MutableList<String>? = null

    var optionComboOneSelection: String? = null
    var optionComboTwoSelection: String? = null
    var optionComboThreeSelection: String? = null
    var optionDescription: String? = null

    var isCoPartFm: Boolean = false

    val token: String = sessionManager.getToken()

    // =====================================================================
    // Observables
    // =====================================================================
    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _comboOne = MutableLiveData<CoParticipationModel>()
    val comboOne: LiveData<CoParticipationModel> = _comboOne

    private val _comboTwo = MutableLiveData<CoParticipationModel>()
    val comboTwo: LiveData<CoParticipationModel> = _comboTwo

    private val _comboThree = MutableLiveData<CoParticipationModel>()
    val comboThree: LiveData<CoParticipationModel> = _comboThree

    /** Evento único com o resultado da pesquisa (navegação Filtros → Itens consumida uma vez). */
    private val _itemsCoParticipation = SingleLiveEvent<CoParticipationItems>()
    val itemsCoParticipation: LiveData<CoParticipationItems> = _itemsCoParticipation

    // =====================================================================
    // API
    // =====================================================================
    fun onGetComboOne() {
        viewModelScope.launch {
            _loading.value = true
            try {
                _comboOne.value = appRepository.onPostCoParticipationCombos(CoParticipationBodyCombo(token, 1))
            } catch (e: Exception) {
                LogManager.e(TAG, "onGetComboOne error => ${e.message}", e)
            } finally {
                _loading.value = false
            }
        }
    }

    fun onGetComboTwo() {
        viewModelScope.launch {
            _loading.value = true
            try {
                _comboTwo.value = appRepository.onPostCoParticipationCombos(CoParticipationBodyCombo(token, 2))
            } catch (e: Exception) {
                LogManager.e(TAG, "onGetComboTwo error => ${e.message}", e)
            } finally {
                _loading.value = false
            }
        }
    }

    fun onGetComboThree() {
        viewModelScope.launch {
            _loading.value = true
            try {
                _comboThree.value = appRepository.onPostCoParticipationCombos(CoParticipationBodyCombo(token, 3))
            } catch (e: Exception) {
                LogManager.e(TAG, "onGetComboThree error => ${e.message}", e)
            } finally {
                _loading.value = false
            }
        }
    }

    fun onGetItemCoParticipation() {
        viewModelScope.launch {
            _loading.value = true
            try {
                _itemsCoParticipation.value = appRepository.onPostCoParticipationValues(
                    CoParticipationBodyValue(
                        token = sessionManager.getToken(),
                        codeComboOne = getCodeComboOne()?.toInt(),
                        codeComboTwo = getCodeComboTwo()?.toInt(),
                        codeComboThree = getCodeComboThree(),
                        description = optionDescription
                    )
                )
            } catch (e: Exception) {
                LogManager.e(TAG, "onGetItemCoParticipation error => ${e.message}", e)
            } finally {
                _loading.value = false
            }
        }
    }

    // =====================================================================
    // Regras da tela
    // =====================================================================
    private fun getCodeComboOne(): String? =
        comboOne.value?.items?.firstOrNull { it.description == optionComboOneSelection }?.code

    private fun getCodeComboTwo(): String? =
        comboTwo.value?.items?.firstOrNull { it.description == optionComboTwoSelection }?.code

    private fun getCodeComboThree(): String? =
        comboThree.value?.items?.firstOrNull { it.description == optionComboThreeSelection }?.code

    fun onClearSelectedComboOne() {
        optionComboOneSelection = null
    }

    fun onClearSelectedComboTwo() {
        optionComboTwoSelection = null
    }

    fun onClearSelectedComboThree() {
        optionComboThreeSelection = null
    }

    fun isEnabledButtonSearch(): Boolean =
        !optionComboOneSelection.isNullOrEmpty() ||
            !optionComboTwoSelection.isNullOrEmpty() ||
            !optionComboThreeSelection.isNullOrEmpty() ||
            !optionDescription.isNullOrEmpty()

    private companion object {
        const val TAG = "CoParticipationViewModel"
    }
}
