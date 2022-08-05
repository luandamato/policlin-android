package com.policlinsaude.newfeature.data.repositories

import com.policlinsaude.newfeature.data.networking.RetrofitInstance
import com.policlinsaude.newfeature.data.services.TicketService
import com.policlinsaude.newfeature.features.tickets.data.models.TicketBodyModel
import com.policlinsaude.newfeature.features.tickets.data.models.TicketModel
import com.policlinsaude.newfeature.utils.makeRequest
import kotlinx.coroutines.coroutineScope

class TicketRepositoryImpl: TicketRepository {

    override suspend fun onGetTickets(ticketBody: TicketBodyModel): TicketModel {
        return coroutineScope {
            makeRequest {
                RetrofitInstance().create(TicketService::class.java)
                    .getTickets(ticketBody)
            }
        }
    }

}