package br.com.policlinsaude.ui.medicalguide

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.policlinsaude.domain.model.MedicalGuide
import br.com.policlinsaude.domain.usecase.GetMedicalGuidesUseCase
import br.com.policlinsaude.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

@HiltViewModel
class MedicalGuideViewModel @Inject constructor(
    private val getMedicalGuidesUseCase: GetMedicalGuidesUseCase
) : BaseViewModel() {

    private val _medicalGuides = MutableLiveData<List<MedicalGuide>>()
    val medicalGuides: LiveData<List<MedicalGuide>> = _medicalGuides

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getMedicalGuides(token: String, filters: Map<String, String> = emptyMap()) {
        _isLoading.value = true
        getMedicalGuidesUseCase(token, filters)
            .doOnComplete { _isLoading.postValue(false) }
            .doOnError { _isLoading.postValue(false) }
            .subscribe(
                { guides -> _medicalGuides.postValue(guides) },
                { error -> _error.postValue(error.message ?: "Erro desconhecido") }
            )
            .addTo(disposables)
    }
}
