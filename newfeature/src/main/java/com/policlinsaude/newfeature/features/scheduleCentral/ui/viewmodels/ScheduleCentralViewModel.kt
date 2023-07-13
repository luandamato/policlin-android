package com.policlinsaude.newfeature.features.incometax.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.policlinsaude.newfeature.data.networking.ServerErrorResponse
import com.policlinsaude.newfeature.data.networking.ViewModelResponse
import com.policlinsaude.newfeature.data.repositories.TicketRepository
import com.policlinsaude.newfeature.features.incometax.data.models.ScheduleCentralBodyModel
import com.policlinsaude.newfeature.features.incometax.data.models.ScheduleCentralResponseModel
import com.policlinsaude.newfeature.utils.SharedPreferences
import kotlinx.coroutines.launch

class ScheduleCentralViewModel(
    val repository: TicketRepository,
    val preferences: SharedPreferences
): ViewModel() {

    private var token: String = preferences.getToken()
    var email = ""
    var phone = ""
    var wpp = ""

    private val _response: MutableLiveData<ViewModelResponse<ScheduleCentralResponseModel, ServerErrorResponse>> = MutableLiveData()
    val data: LiveData<ViewModelResponse<ScheduleCentralResponseModel, ServerErrorResponse>> get() = _response

    fun onGetData() {
        viewModelScope.launch {
            val viewModelResponse = ViewModelResponse<ScheduleCentralResponseModel, ServerErrorResponse>()
            try {
                _response.postValue(viewModelResponse)
                _response.postValue(
                    viewModelResponse.setData(
                        repository.onGetScheduleCentral(ScheduleCentralBodyModel(token = token))
                    )
                )
            } catch (e: ServerErrorResponse) {
                _response.postValue(viewModelResponse.setError(e))
            }
        }
    }

}