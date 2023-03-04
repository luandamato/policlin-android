package com.policlinsaude.newfeature.data.services

import com.policlinsaude.newfeature.features.incometax.data.models.IncomeTaxBodyModel
import com.policlinsaude.newfeature.features.incometax.data.models.IncomeTaxResponseModel
import com.policlinsaude.newfeature.features.tickets.data.models.TicketBodyModel
import com.policlinsaude.newfeature.features.tickets.data.models.TicketModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TicketService {

    @POST("rest/apiBoletos")
    suspend fun getTickets(
        @Body ticketBody: TicketBodyModel
    ): Response<TicketModel>

    @POST("rest/apiIR")
    suspend fun getIR(
        @Body token: IncomeTaxBodyModel
    ): Response<IncomeTaxResponseModel>

}