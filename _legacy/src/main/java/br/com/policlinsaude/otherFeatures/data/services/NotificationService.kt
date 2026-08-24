package com.policlinsaude.newfeature.data.services

import com.policlinsaude.newfeature.features.notifications.data.models.NotificationDeleteRequest
import com.policlinsaude.newfeature.features.notifications.data.models.NotificationsResponseModel
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface NotificationService {

    @GET("MAPP_Notificacoes")
    suspend fun getNotifications(
        @Header("token") token: String
    ): Response<NotificationsResponseModel>

    @POST("MAPP_Notificacoes")
    suspend fun postNotifications(
        @Header("token") token: String,
        @Body body: NotificationDeleteRequest
    ): Response<NotificationsResponseModel>

}