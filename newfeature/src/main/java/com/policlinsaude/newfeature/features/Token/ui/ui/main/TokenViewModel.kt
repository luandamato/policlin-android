package com.policlinsaude.newfeature.features.Token.ui.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.policlinsaude.newfeature.data.networking.ServerErrorResponse
import com.policlinsaude.newfeature.data.networking.ViewModelResponse
import com.policlinsaude.newfeature.data.repositories.TicketRepository
import com.policlinsaude.newfeature.features.Token.models.TokenBodyModel
import com.policlinsaude.newfeature.features.Token.models.TokenResponseModel
import com.policlinsaude.newfeature.utils.SharedPreferences
import kotlinx.coroutines.launch

class TokenViewModel(
val repository: TicketRepository,
val preferences: SharedPreferences
): ViewModel() {

    private var token: String = preferences.getToken()
    var serviceToken = ""
    var validade = 2

    private val _response: MutableLiveData<ViewModelResponse<TokenResponseModel, ServerErrorResponse>> = MutableLiveData()
    val data: LiveData<ViewModelResponse<TokenResponseModel, ServerErrorResponse>> get() = _response

    fun onGetData() {
        viewModelScope.launch {
            val viewModelResponse = ViewModelResponse<TokenResponseModel, ServerErrorResponse>()
            try {
                _response.postValue(viewModelResponse)
                _response.postValue(
                    viewModelResponse.setData(
                        repository.onGetToken(TokenBodyModel(token = token))
                    )
                )
            } catch (e: ServerErrorResponse) {
                _response.postValue(viewModelResponse.setError(e))
            }
        }
    }

}