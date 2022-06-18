package com.policlinsaude.newfeature.data.repositories

import com.policlinsaude.newfeature.features.tickets.data.models.TicketBodyModel
import com.policlinsaude.newfeature.features.tickets.data.models.TicketModel

interface TicketRepository {

    suspend fun onGetTickets(ticketBody: TicketBodyModel): TicketModel

}