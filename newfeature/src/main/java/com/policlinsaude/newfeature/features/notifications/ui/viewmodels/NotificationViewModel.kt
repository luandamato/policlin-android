package com.policlinsaude.newfeature.features.notifications.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.policlinsaude.newfeature.data.networking.ServerErrorResponse
import com.policlinsaude.newfeature.data.networking.ViewModelResponse
import com.policlinsaude.newfeature.data.repositories.NotificationRepository
import com.policlinsaude.newfeature.features.notifications.data.models.NotificationDeleteRequest
import com.policlinsaude.newfeature.features.notifications.data.models.NotificationsModel
import com.policlinsaude.newfeature.features.notifications.data.models.NotificationsResponseModel
import com.policlinsaude.newfeature.utils.SharedPreferences
import kotlinx.coroutines.launch
import okhttp3.ResponseBody

class NotificationViewModel(
    val preferences: SharedPreferences,
    val repository: NotificationRepository
): ViewModel() {

    private val _responseNotifications: MutableLiveData<ViewModelResponse<NotificationsResponseModel, ServerErrorResponse>> = MutableLiveData()
    val notifications: LiveData<ViewModelResponse<NotificationsResponseModel, ServerErrorResponse>> get() = _responseNotifications

    private val _responseNotificationsDelete: MutableLiveData<ViewModelResponse<NotificationsResponseModel, ServerErrorResponse>> = MutableLiveData()
    val notificationsDelete: LiveData<ViewModelResponse<NotificationsResponseModel, ServerErrorResponse>> get() = _responseNotificationsDelete

    private val token: String = preferences.getToken()

    val notificationsSelected: MutableLiveData<MutableList<NotificationsModel>> = MutableLiveData(arrayListOf())

    fun addSelectedNotification(item: NotificationsModel) {
        notificationsSelected.value?.add(item)
    }

    fun removeSelectedNotification(item: NotificationsModel) {
        notificationsSelected.value?.remove(item)
    }

    fun addAllItems() {
        notificationsSelected.value = notifications.value?.getData()?.list ?: arrayListOf()
    }

    fun clearSelectedNotifications() {
        notificationsSelected.value = arrayListOf()
    }

    fun onGetNotifications() {
        viewModelScope.launch {
            val viewModelResponse = ViewModelResponse<NotificationsResponseModel, ServerErrorResponse>()
            try {
                _responseNotifications.postValue(viewModelResponse)
                /*val x = viewModelResponse.setData(

                    NotificationsResponseModel(
                        codAcao = 1,
                        list = arrayListOf(
                            NotificationsModel("0", "Teste 1", "teste", "24/0505"),
                            NotificationsModel("1", "Teste 2", "teste", "24/0505"),
                            NotificationsModel("2", "Teste 3", "teste", "24/0505"),
                            NotificationsModel("3", "Teste 4", "teste", "24/0505"),
                            NotificationsModel("4", "Teste 5", "teste", "24/0505"),
                            NotificationsModel("5", "Teste 6", "teste", "24/0505")
                        )
                    )
                )*/
                _responseNotifications.postValue(
                    viewModelResponse.setData(
                        repository.onGetNotifications(token = token)
                    )
                )
            } catch (e: ServerErrorResponse) {
                _responseNotifications.postValue(viewModelResponse.setError(e))
            }
        }
    }

    fun onDeleteNotifications() {
        viewModelScope.launch {
            val viewModelResponse = ViewModelResponse<NotificationsResponseModel, ServerErrorResponse>()
            try {
                val body = NotificationDeleteRequest()
                notificationsSelected.value?.forEachIndexed { index, notificationsModel ->
                    if(index == notificationsSelected.value!!.lastIndex)
                       body.id += notificationsModel.id
                    else
                       body.id += "${notificationsModel.id},"
                }
                _responseNotificationsDelete.postValue(viewModelResponse)
                _responseNotificationsDelete.postValue(
                    viewModelResponse.setData(
                        repository.onDeleteNotifications(token = token, body = body)
                    )
                )
            } catch (e: ServerErrorResponse) {
                _responseNotificationsDelete.postValue(viewModelResponse.setError(e))
            }
        }
    }

}