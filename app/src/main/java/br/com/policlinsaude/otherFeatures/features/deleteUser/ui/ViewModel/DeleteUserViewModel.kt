package com.policlinsaude.newfeature.features.deleteUser.ui.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.policlinsaude.newfeature.data.networking.ServerErrorResponse
import com.policlinsaude.newfeature.data.networking.ViewModelResponse
import com.policlinsaude.newfeature.data.repositories.TicketRepository
import com.policlinsaude.newfeature.features.deleteUser.model.DeleteUserRequest
import com.policlinsaude.newfeature.features.guidAuthorizer.data.models.ComumModel
import com.policlinsaude.newfeature.utils.SharedPreferences
import kotlinx.coroutines.launch

class DeleteUserViewModel (
    val repository: TicketRepository,
    val preferences: SharedPreferences
): ViewModel() {

    private var token: String = preferences.getToken()
    private val _responseUser: MutableLiveData<ViewModelResponse<ComumModel, ServerErrorResponse>> =
        MutableLiveData()
    private val _logout: MutableLiveData<ViewModelResponse<ComumModel, ServerErrorResponse>> =
        MutableLiveData()
    val data: LiveData<ViewModelResponse<ComumModel, ServerErrorResponse>> get() = _responseUser
    val logout: LiveData<ViewModelResponse<ComumModel, ServerErrorResponse>> get() = _logout


    fun deleteUser() {
        viewModelScope.launch {
            val viewModelResponse = ViewModelResponse<ComumModel, ServerErrorResponse>()
            try {
                _responseUser.postValue(viewModelResponse)
                _responseUser.postValue(
                    viewModelResponse.setData(
                        repository.deleteUser(DeleteUserRequest(token = token))
                    )
                )
            } catch (e: ServerErrorResponse) {
                _responseUser.postValue(viewModelResponse.setError(e))
            }
        }
    }

    fun logout(){
        viewModelScope.launch {
            val viewModelResponse = ViewModelResponse<ComumModel, ServerErrorResponse>()
            try {
                _logout.postValue(viewModelResponse)
                _logout.postValue(
                    viewModelResponse.setData(
                        repository.logout(DeleteUserRequest(token = token))
                    )
                )
            } catch (e: ServerErrorResponse) {
                _logout.postValue(viewModelResponse.setError(e))
            }
        }
    }

    fun removeToken(){
        preferences.removeToken()
    }
}