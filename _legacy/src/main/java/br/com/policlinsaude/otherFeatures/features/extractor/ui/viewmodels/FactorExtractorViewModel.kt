package com.policlinsaude.newfeature.features.extractor.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.policlinsaude.newfeature.data.models.UserModel
import com.policlinsaude.newfeature.data.networking.ServerErrorResponse
import com.policlinsaude.newfeature.data.networking.ViewModelResponse
import com.policlinsaude.newfeature.data.repositories.FactorExtractorRepository
import com.policlinsaude.newfeature.features.extractor.data.models.*
import com.policlinsaude.newfeature.utils.SharedPreferences
import kotlinx.coroutines.launch

class FactorExtractorViewModel(
    val repository: FactorExtractorRepository,
    val preferences: SharedPreferences
): ViewModel() {

    var selectedYear: String? = null
    var selectMonth: String? = null

    var listYears: MutableList<String> = arrayListOf()
    var listMonths: MutableList<String> = arrayListOf()

    private val token: String = preferences.getToken()

    var isCoPartFm: Boolean = false

    private val _responseExtractor: MutableLiveData<ViewModelResponse<FactorExtractorModel, ServerErrorResponse>> = MutableLiveData()
    val extractor: LiveData<ViewModelResponse<FactorExtractorModel, ServerErrorResponse>> get() = _responseExtractor

    private val _responseYear: MutableLiveData<ViewModelResponse<FactorExtractorYearsModel, ServerErrorResponse>> = MutableLiveData()
    val year: LiveData<ViewModelResponse<FactorExtractorYearsModel, ServerErrorResponse>> get() = _responseYear

    private val _responseMonths: MutableLiveData<ViewModelResponse<FactorExtractorMonthsModel, ServerErrorResponse>> = MutableLiveData()
    val months: LiveData<ViewModelResponse<FactorExtractorMonthsModel, ServerErrorResponse>> get() = _responseMonths

    private val _responseUser: MutableLiveData<ViewModelResponse<UserModel, ServerErrorResponse>> = MutableLiveData()
    val user: LiveData<ViewModelResponse<UserModel, ServerErrorResponse>> get() = _responseUser

    fun onGetFactorsExtractors() {
        viewModelScope.launch {
            val viewModelResponse = ViewModelResponse<FactorExtractorModel, ServerErrorResponse>()
            try {
                _responseExtractor.postValue(viewModelResponse)
                _responseExtractor.postValue(
                    viewModelResponse.setData(
                        repository.onPostCoParticipationExtractor(FactorExtractorBodyModel(token = token, year = selectedYear?.toInt(), month = selectMonth?.toInt()))
                    )
                )
            } catch (e: ServerErrorResponse) {
                _responseExtractor.postValue(viewModelResponse.setError(e))
            }
        }
    }

    fun onGetUser() {
        viewModelScope.launch {
            val viewModelResponse = ViewModelResponse<UserModel, ServerErrorResponse>()
            try {
                _responseUser.postValue(viewModelResponse)
                _responseUser.postValue(
                    viewModelResponse.setData(
                        repository.onGetProfile(token = token, verify = 1)
                    )
                )
            } catch (e: ServerErrorResponse) {
                _responseUser.postValue(viewModelResponse.setError(e))
            }
        }
    }

    fun onGetFactorsExtractorsYears() {
        viewModelScope.launch {
            val viewModelResponse = ViewModelResponse<FactorExtractorYearsModel, ServerErrorResponse>()
            try {
                _responseYear.postValue(viewModelResponse)
                _responseYear.postValue(
                    viewModelResponse.setData(
                        repository.onGetYearsExtractor()
                    )
                )
            } catch (e: ServerErrorResponse) {
                _responseYear.postValue(viewModelResponse.setError(e))
            }
        }
    }

    fun onGetFactorsExtractorsMonths() {
        viewModelScope.launch {
            val viewModelResponse = ViewModelResponse<FactorExtractorMonthsModel, ServerErrorResponse>()
            try {
                _responseMonths.postValue(viewModelResponse)
                _responseMonths.postValue(
                    viewModelResponse.setData(
                        repository.onGetMonthsExtractor(FactorExtractorMonthsBody(token = token, year = selectedYear?.toInt() ?: 0))
                    )
                )
            } catch (e: ServerErrorResponse) {
                _responseMonths.postValue(viewModelResponse.setError(e))
            }
        }
    }

    fun clearMonthsSelected() {
        selectMonth = null
    }

    fun clearYearSelected() {
        selectedYear = null
    }

    fun enableButton(): Boolean = !selectedYear.isNullOrEmpty()


}