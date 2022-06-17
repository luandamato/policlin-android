package com.policlinsaude.newfeature.data.repositories

import com.policlinsaude.newfeature.data.networking.RetrofitInstance
import com.policlinsaude.newfeature.data.services.TicketService
import com.policlinsaude.newfeature.features.tickets.data.models.TicketModel
import com.policlinsaude.newfeature.utils.makeRequest
import kotlinx.coroutines.coroutineScope

class TicketRepositoryImpl: TicketRepository {

    override suspend fun onGetTickets(token: String, opcao: Int, ano: Int, mes: Int): TicketModel {
        return coroutineScope {
            makeRequest {
                RetrofitInstance().create(TicketService::class.java)
                    .getTickets(token = token, opcao = opcao, ano = ano, mes = mes)
            }
        }
    }

}