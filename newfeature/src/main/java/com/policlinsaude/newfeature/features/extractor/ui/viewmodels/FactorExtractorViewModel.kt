package com.policlinsaude.newfeature.features.extractor.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.policlinsaude.newfeature.data.networking.ServerErrorResponse
import com.policlinsaude.newfeature.data.networking.ViewModelResponse
import com.policlinsaude.newfeature.data.repositories.FactorExtractorRepository
import com.policlinsaude.newfeature.features.extractor.data.models.FactorExtractorModel
import com.policlinsaude.newfeature.features.extractor.data.models.FactorExtractorBodyModel
import kotlinx.coroutines.launch
import java.util.*

class FactorExtractorViewModel(
    val repository: FactorExtractorRepository
): ViewModel() {

    var year: Int = Calendar.getInstance().get(Calendar.YEAR)
    var month: Int = 0

    private val _responseExtractor: MutableLiveData<ViewModelResponse<FactorExtractorModel, ServerErrorResponse>> = MutableLiveData()
    val extractor: LiveData<ViewModelResponse<FactorExtractorModel, ServerErrorResponse>> get() = _responseExtractor

    fun onGetFactorsExtractors() {
        viewModelScope.launch {
            val viewModelResponse = ViewModelResponse<FactorExtractorModel, ServerErrorResponse>()
            try {
                _responseExtractor.postValue(viewModelResponse)
                _responseExtractor.postValue(
                    viewModelResponse.setData(
                        repository.onPostCoParticipationExtractor(FactorExtractorBodyModel(year = year, month = month))
                    )
                )
            } catch (e: ServerErrorResponse) {
                _responseExtractor.postValue(viewModelResponse.setError(e))
            }
        }
    }

    fun clearDates() {
        year = Calendar.getInstance().get(Calendar.YEAR)
        month = 0
    }
}