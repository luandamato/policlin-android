package com.policlinsaude.newfeature.features.Token.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.policlinsaude.newfeature.data.models.UserModel
import com.policlinsaude.newfeature.data.networking.ServerErrorResponse
import com.policlinsaude.newfeature.data.networking.ViewModelResponse
import com.policlinsaude.newfeature.data.repositories.TicketRepository
import com.policlinsaude.newfeature.features.Token.models.BeneficiariosRequestModel
import com.policlinsaude.newfeature.features.Token.models.BeneficiariosResponseModel
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
    var ordem = ""

    private val _response: MutableLiveData<ViewModelResponse<TokenResponseModel, ServerErrorResponse>> = MutableLiveData()
    val data: LiveData<ViewModelResponse<TokenResponseModel, ServerErrorResponse>> get() = _response

    private val _dependets: MutableLiveData<ViewModelResponse<BeneficiariosResponseModel, ServerErrorResponse>> = MutableLiveData()
    val dependets: LiveData<ViewModelResponse<BeneficiariosResponseModel, ServerErrorResponse>> get() = _dependets

    private val _responseUser: MutableLiveData<ViewModelResponse<UserModel, ServerErrorResponse>> = MutableLiveData()
    val user: LiveData<ViewModelResponse<UserModel, ServerErrorResponse>> get() = _responseUser

    val userSelected: MutableLiveData<String> = MutableLiveData()


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
                setUser(user.value!!.getData()!!.name!!, user.value!!.getData()!!.register!!, "0${user.value!!.getData()!!.order!!}")
            } catch (e: ServerErrorResponse) {
                _responseUser.postValue(viewModelResponse.setError(e))
            }
        }
    }

    fun onGetData() {
        viewModelScope.launch {
            val viewModelResponse = ViewModelResponse<TokenResponseModel, ServerErrorResponse>()
            try {
                _response.postValue(viewModelResponse)
                _response.postValue(
                    viewModelResponse.setData(
                        repository.onGetToken(TokenBodyModel(token = token, ordemToken = ordem))
                    )
                )
            } catch (e: ServerErrorResponse) {
                _response.postValue(viewModelResponse.setError(e))
            }
        }
    }
    fun onGetDependents() {
        viewModelScope.launch {
            val viewModelResponse = ViewModelResponse<BeneficiariosResponseModel, ServerErrorResponse>()
            try {
                _dependets.postValue(viewModelResponse)
                _dependets.postValue(
                    viewModelResponse.setData(
                        repository.onGetDependents(BeneficiariosRequestModel(token = token))
                    )
                )
            } catch (e: ServerErrorResponse) {
                _dependets.postValue(viewModelResponse.setError(e))
            }
        }
    }

    fun setUser(nome: String, matricula: String, ordem: String){
        this.ordem = ordem
        userSelected.value = "$nome $matricula-$ordem"
    }

    fun setDefaultUser(){
        onGetUser()
    }

}