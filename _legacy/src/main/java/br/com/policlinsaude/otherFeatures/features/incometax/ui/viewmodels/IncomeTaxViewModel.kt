package com.policlinsaude.newfeature.features.incometax.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.policlinsaude.newfeature.data.networking.ServerErrorResponse
import com.policlinsaude.newfeature.data.networking.ViewModelResponse
import com.policlinsaude.newfeature.data.repositories.TicketRepository
import com.policlinsaude.newfeature.features.incometax.data.models.IncomeTaxBodyModel
import com.policlinsaude.newfeature.features.incometax.data.models.IncomeTaxResponseModel
import com.policlinsaude.newfeature.utils.SharedPreferences
import kotlinx.coroutines.launch
import okhttp3.ResponseBody

class IncomeTaxViewModel(
    val repository: TicketRepository,
    val preferences: SharedPreferences
): ViewModel() {

    private var token: String = preferences.getToken()

    private val _responseIr: MutableLiveData<ViewModelResponse<IncomeTaxResponseModel, ServerErrorResponse>> = MutableLiveData()
    val incomeTax: LiveData<ViewModelResponse<IncomeTaxResponseModel, ServerErrorResponse>> get() = _responseIr

    fun onGetIncomeTax() {
        viewModelScope.launch {
            val viewModelResponse = ViewModelResponse<IncomeTaxResponseModel, ServerErrorResponse>()
            try {
                _responseIr.postValue(viewModelResponse)
                _responseIr.postValue(
                    viewModelResponse.setData(
                        repository.onGetIncomeTax(IncomeTaxBodyModel(token = token))
                    )
                )
            } catch (e: ServerErrorResponse) {
                _responseIr.postValue(viewModelResponse.setError(e))
            }
        }
    }

}