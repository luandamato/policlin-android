package br.com.policlinsaude.ui.fragments.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.policlinsaude.data.local.SessionManager
import br.com.policlinsaude.data.models.toBanners
import br.com.policlinsaude.data.repositories.AppRepository
import br.com.policlinsaude.domain.models.Banner
import br.com.policlinsaude.domain.models.Person
import br.com.policlinsaude.domain.models.ValidateButtonBody
import br.com.policlinsaude.domain.models.ValidateButtons
import br.com.policlinsaude.util.helpers.SingleLiveEvent
import kotlinx.coroutines.launch

/**
 * Eventos únicos del HomeFragment.
 */
sealed interface HomeEvent {
    data object ShowLoginDialog : HomeEvent
    data class ShowError(val message: String) : HomeEvent
    data class ShowUpdateApp(val message: String, val force: Boolean) : HomeEvent
}

/**
 * ViewModel de la pantalla home: banners + validaciones + tiles dinámicos.
 * Migrado de `_legacy/.../home/presenter/HomePresenterImpl.kt`.
 */
class HomeViewModel(
    private val appRepository: AppRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _person = MutableLiveData<Person?>(null)
    val person: LiveData<Person?> = _person

    private val _banners = MutableLiveData<List<Banner>>(mutableListOf())
    val banners: LiveData<List<Banner>> = _banners

    private val _options = MutableLiveData<MutableList<HomeOptionEnum>>(defaultOptions())
    val options: LiveData<MutableList<HomeOptionEnum>> = _options

    private val _bannerLoading = MutableLiveData(false)
    val bannerLoading: LiveData<Boolean> = _bannerLoading

    private val _loading = MutableLiveData(true)
    val loading: LiveData<Boolean> = _loading

    private val _forceUpdate = MutableLiveData(false)
    val forceUpdate: LiveData<Boolean> = _forceUpdate

    private val _event = SingleLiveEvent<HomeEvent>()
    val event: LiveData<HomeEvent> = _event

    private var isCoPartFM = false

    /** Inicializa la pantalla: persona, banners, buttons. */
    fun onViewAttached() {
        loadPersonAndValidate()
        loadBanners()
    }

    fun isCoPartFMValue(): Boolean = isCoPartFM

    fun onMenuClickedAsGuest() {
        _event.value = HomeEvent.ShowLoginDialog
    }

    private fun loadPersonAndValidate() {
        val person = sessionManager.getPerson()
        _person.value = person
        if (person == null) {
            _event.value = HomeEvent.ShowLoginDialog
            _loading.value = false
            return
        }
        validateUser(person)
        validateButtonsVisible()
    }
// =====================================================================
    // Validaciones
    // =====================================================================
    private fun validateUser(person: Person) {
        val token = sessionManager.getToken()
        viewModelScope.launch {
            try {
                val response = appRepository.onValidateUserConnected(
                    person.plan.register,
                    person.plan.order,
                    token
                )
                when (response.codAcao) {
                    450 -> _event.value = HomeEvent.ShowUpdateApp(response.msgExterna.orEmpty(), false)
                    455 -> {
                        _forceUpdate.value = true
                        _event.value = HomeEvent.ShowUpdateApp(response.msgExterna.orEmpty(), true)
                    }
                    5 -> {
                        sessionManager.clearSession()
                        _event.value = HomeEvent.ShowLoginDialog
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "HomeVM: validateUser error => ${e.message}")
                _event.value = HomeEvent.ShowError(e.message.orEmpty())
            }
        }
    }

    private fun validateButtonsVisible() {
        val token = sessionManager.getToken()
        viewModelScope.launch {
            try {
                val buttons = appRepository.onValidateButtons(ValidateButtonBody(token))
                applyButtonVisibility(buttons)
            } catch (e: Exception) {
                Log.e(TAG, "HomeVM: validateButtons error => ${e.message}")
                applyButtonVisibility(ValidateButtons())
            }
        }
    }

    private fun applyButtonVisibility(buttons: ValidateButtons) {
        val list = defaultOptions()
        if (!buttons.boleto) list.remove(HomeOptionEnum.TICKET)
        if (buttons.copartFM.isEmpty()) {
            list.remove(HomeOptionEnum.RESEARCH_VALUES_CO_PARTICIPATION)
            list.remove(HomeOptionEnum.FACTOR_EXTRACTOR)
        }
        if (!buttons.IR) list.remove(HomeOptionEnum.INCOME_TAX)
        if (!buttons.central) list.remove(HomeOptionEnum.SCHEDULE)
        if (!buttons.autorizador) list.remove(HomeOptionEnum.GUIDE_AUTHORIZER)
        if (!buttons.gerarToken) list.remove(HomeOptionEnum.SERVICE_TOKEN)

        isCoPartFM = buttons.copartFM.lowercase() == CO_PART_FM
        sessionManager.putBoolean(KEY_SELECAO, buttons.selecaoBeneficiarioAutorizador)

        _options.value = list
        _loading.value = false
    }

    // =====================================================================
    // Banners
    // =====================================================================
    private fun loadBanners() {
        viewModelScope.launch {
            _bannerLoading.value = true
            try {
                val response = appRepository.onGetBanners(url = BANNERS_URL, returnImage = true, id = 0)
                _banners.value = response.toBanners()
            } catch (e: Exception) {
                Log.e(TAG, "HomeVM: loadBanners error => ${e.message}")
                _banners.value = mutableListOf()
            } finally {
                _bannerLoading.value = false
            }
        }
    }

    private companion object {
        const val TAG = "HomeViewModel"
        const val CO_PART_FM = "fm"
        const val BANNERS_URL = "http://mpoli.tangram.net.br/appplcsa/api/Banner"
        const val KEY_SELECAO = "selecaoBeneficiarioAutorizador"

        fun defaultOptions(): MutableList<HomeOptionEnum> = mutableListOf(
            HomeOptionEnum.SCHEDULE,
            HomeOptionEnum.MEDICAL_GUIDE,
            HomeOptionEnum.HEALTH_INSURANCE,
            HomeOptionEnum.TICKET,
            HomeOptionEnum.FAVORITES,
            HomeOptionEnum.OWN_NETWORK,
            HomeOptionEnum.FACTOR_EXTRACTOR,
            HomeOptionEnum.RESEARCH_VALUES_CO_PARTICIPATION,
            HomeOptionEnum.INCOME_TAX,
            HomeOptionEnum.GUIDE_AUTHORIZER,
            HomeOptionEnum.SERVICE_TOKEN
        )
    }
}