package br.com.policlinsaude.ui.activities.factorExtractor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.policlinsaude.data.local.SessionManager
import br.com.policlinsaude.data.models.FactorExtractorBodyModel
import br.com.policlinsaude.data.models.FactorExtractorDetailItemsModel
import br.com.policlinsaude.data.models.FactorExtractorModel
import br.com.policlinsaude.data.models.FactorExtractorMonthsBody
import br.com.policlinsaude.data.models.UserModel
import br.com.policlinsaude.data.repositories.AppRepository
import br.com.policlinsaude.util.extensions.getMonthName
import br.com.policlinsaude.util.helpers.SingleLiveEvent
import br.com.policlinsaude.utils.LogManager
import kotlinx.coroutines.launch

sealed interface FactorExtractorEvent {
    data class ShowError(val message: String) : FactorExtractorEvent
}

class FactorExtractorViewModel(
    private val appRepository: AppRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    var selectedYear: String? = null
    var selectMonth: String? = null
    var listYears: MutableList<String> = arrayListOf()
    var listMonths: MutableList<String> = arrayListOf()
    var isCoPartFm: Boolean = false

    private val token: String = sessionManager.getToken()

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _years = MutableLiveData<List<String>>(emptyList())
    val years: LiveData<List<String>> = _years

    private val _months = MutableLiveData<List<String>>(emptyList())
    val months: LiveData<List<String>> = _months

    private val _extractor = MutableLiveData<FactorExtractorModel>()
    val extractor: LiveData<FactorExtractorModel> = _extractor

    private val _user = MutableLiveData<UserModel>()
    val user: LiveData<UserModel> = _user

    private val _event = SingleLiveEvent<FactorExtractorEvent>()
    val event: LiveData<FactorExtractorEvent> = _event

    fun onGetFactorsExtractorsYears() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = appRepository.onGetFactorExtractorYears()
                val mappedYears = response.sdtValores.map { it.valor.toString() }.toMutableList()
                listYears.clear()
                listYears.addAll(mappedYears)
                _years.value = mappedYears
            } catch (e: Exception) {
                LogManager.e(TAG, "onGetFactorsExtractorsYears error => ${e.message}", e)
                _event.value = FactorExtractorEvent.ShowError(
                    e.message?.takeIf { it.isNotBlank() } ?: "Não foi possível carregar os anos."
                )
            } finally {
                _loading.value = false
            }
        }
    }

    fun onGetFactorsExtractorsMonths() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val body = FactorExtractorMonthsBody(
                    token = sessionManager.getToken(),
                    year = selectedYear?.toIntOrNull() ?: 0
                )
                val response = appRepository.onGetFactorExtractorMonths(body)
                val mappedMonths = response.sdtValores.map { it.valor.getMonthName() }.toMutableList()
                listMonths.clear()
                listMonths.addAll(mappedMonths)
                _months.value = mappedMonths
            } catch (e: Exception) {
                LogManager.e(TAG, "onGetFactorsExtractorsMonths error => ${e.message}", e)
                _event.value = FactorExtractorEvent.ShowError(
                    e.message?.takeIf { it.isNotBlank() } ?: "Não foi possível carregar os meses."
                )
            } finally {
                _loading.value = false
            }
        }
    }

    fun onGetFactorsExtractors() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val body = FactorExtractorBodyModel(
                    token = sessionManager.getToken(),
                    year = selectedYear?.toIntOrNull(),
                    month = selectMonth?.toIntOrNull()
                )
                val response = appRepository.onPostFactorExtractor(body)
                _extractor.value = response
            } catch (e: Exception) {
                LogManager.e(TAG, "onGetFactorsExtractors error => ${e.message}", e)
                _event.value = FactorExtractorEvent.ShowError(
                    e.message?.takeIf { it.isNotBlank() } ?: "Não foi possível consultar o extrato."
                )
            } finally {
                _loading.value = false
            }
        }
    }

    fun onGetUser() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = appRepository.onGetProfile(token, verify = 1)
                _user.value = response
            } catch (e: Exception) {
                LogManager.e(TAG, "onGetUser error => ${e.message}", e)
            } finally {
                _loading.value = false
            }
        }
    }

    fun clearMonthsSelected() {
        selectMonth = null
    }

    fun clearYearSelected() {
        selectedYear = null
        selectMonth = null
    }

    fun enableButton(): Boolean = !selectedYear.isNullOrEmpty() && !selectMonth.isNullOrEmpty()

    private companion object {
        const val TAG = "FactorExtractorViewModel"
    }
}
