package br.com.policlinsaude.ui.fragments.units

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.policlinsaude.data.models.PresentationEstablishment
import br.com.policlinsaude.data.models.PresentationQualification
import br.com.policlinsaude.data.models.toPresentation
import br.com.policlinsaude.data.repositories.AppRepository
import br.com.policlinsaude.util.helpers.SingleLiveEvent
import kotlinx.coroutines.launch

sealed interface UnitsEvent {
    data class ShowError(val message: String) : UnitsEvent
    data class NavigateToDetails(val establishment: PresentationEstablishment) : UnitsEvent
}

class UnitsViewModel(
    private val appRepository: AppRepository
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _units = MutableLiveData<List<PresentationEstablishment>>()
    val units: LiveData<List<PresentationEstablishment>> = _units

    private val _qualifications = MutableLiveData<List<PresentationQualification>>()
    val qualifications: LiveData<List<PresentationQualification>> = _qualifications

    private val _event = SingleLiveEvent<UnitsEvent>()
    val event: LiveData<UnitsEvent> = _event

    fun getUnits() {
        viewModelScope.launch {
            _loading.value = true
            try {
                // AppRepository.onGetUnits() no app ya existe y llama al service
                val response = appRepository.onGetUnits(null)
                
                val mappedUnits = response.establishments?.map { it.toPresentation() } ?: emptyList()
                val mappedQualifications = response.qualifications?.map { it.toPresentation() } ?: emptyList()

                _units.value = mappedUnits
                _qualifications.value = mappedQualifications
            } catch (e: Exception) {
                _event.value = UnitsEvent.ShowError(e.message ?: "Erro ao carregar unidades")
            } finally {
                _loading.value = false
            }
        }
    }

    fun onItemClick(establishment: PresentationEstablishment) {
        _event.value = UnitsEvent.NavigateToDetails(establishment)
    }
}
