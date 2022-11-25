package com.policlinsaude.newfeature.data.repositories

import com.policlinsaude.newfeature.data.networking.RetrofitInstance
import com.policlinsaude.newfeature.data.services.NotificationService
import com.policlinsaude.newfeature.features.notifications.data.models.NotificationDeleteRequest
import com.policlinsaude.newfeature.features.notifications.data.models.NotificationsResponseModel
import com.policlinsaude.newfeature.utils.makeRequest
import kotlinx.coroutines.coroutineScope
import okhttp3.ResponseBody

class NotificationRepositoryImpl: NotificationRepository {

    override suspend fun onGetNotifications(token: String): NotificationsResponseModel {
        return coroutineScope {
            makeRequest {
                RetrofitInstance().create(NotificationService::class.java, baseUrl = RetrofitInstance.API_NOTIFICATION)
                    .getNotifications(token = token)
            }
        }
    }

    override suspend fun onDeleteNotifications(token: String, body: NotificationDeleteRequest): NotificationsResponseModel {
        return coroutineScope {
            makeRequest {
                RetrofitInstance().create(NotificationService::class.java, baseUrl = RetrofitInstance.API_NOTIFICATION)
                    .postNotifications(token = token, body = body)
            }
        }
    }

}