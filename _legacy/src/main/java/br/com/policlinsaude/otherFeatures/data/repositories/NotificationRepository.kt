package com.policlinsaude.newfeature.data.repositories

import com.policlinsaude.newfeature.features.notifications.data.models.NotificationDeleteRequest
import com.policlinsaude.newfeature.features.notifications.data.models.NotificationsResponseModel
import okhttp3.ResponseBody

interface NotificationRepository {

    suspend fun onGetNotifications(token: String): NotificationsResponseModel

    suspend fun onDeleteNotifications(token: String, body: NotificationDeleteRequest): NotificationsResponseModel

}