package com.policlinsaude.newfeature.data.repositories

import com.policlinsaude.newfeature.features.tickets.data.models.TicketModel

interface TicketRepository {

    suspend fun onGetTickets(token: String, opcao: Int, ano: Int, mes: Int): TicketModel

}