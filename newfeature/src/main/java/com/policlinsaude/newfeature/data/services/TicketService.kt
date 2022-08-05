package com.policlinsaude.newfeature.data.services

import com.policlinsaude.newfeature.features.tickets.data.models.TicketBodyModel
import com.policlinsaude.newfeature.features.tickets.data.models.TicketModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.POST
import retrofit2.http.Query

interface TicketService {

    @POST("rest/apiBoletos")
    suspend fun getTickets(
        @Body ticketBody: TicketBodyModel
    ): Response<TicketModel>

}