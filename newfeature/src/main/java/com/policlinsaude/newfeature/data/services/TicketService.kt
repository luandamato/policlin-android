package com.policlinsaude.newfeature.data.services

import com.policlinsaude.newfeature.features.tickets.data.models.TicketModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TicketService {

    @GET("rest/apiBoletos")
    suspend fun getTickets(
        @Query("token") token: String?,
        @Query("opcao") opcao: Int,
        @Query("ano") ano: Int,
        @Query("mes") mes: Int
    ): Response<TicketModel>

}