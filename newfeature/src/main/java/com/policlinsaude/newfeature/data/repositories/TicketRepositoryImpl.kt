package com.policlinsaude.newfeature.data.repositories

import com.policlinsaude.newfeature.data.models.UserModel
import com.policlinsaude.newfeature.data.networking.RetrofitInstance
import com.policlinsaude.newfeature.data.services.TicketService
import com.policlinsaude.newfeature.features.guidAuthorizer.data.models.GuideAuthorizerRequestModel
import com.policlinsaude.newfeature.features.guidAuthorizer.data.models.GuideAuthorizerResponseModel
import com.policlinsaude.newfeature.features.incometax.data.models.IncomeTaxBodyModel
import com.policlinsaude.newfeature.features.incometax.data.models.IncomeTaxResponseModel
import com.policlinsaude.newfeature.features.scheduleCentral.models.ScheduleCentralBodyModel
import com.policlinsaude.newfeature.features.scheduleCentral.models.ScheduleCentralResponseModel
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

    override suspend fun onGetIncomeTax(token: IncomeTaxBodyModel): IncomeTaxResponseModel {
        return coroutineScope {
            makeRequest {
                RetrofitInstance().create(TicketService::class.java)
                    .getIR(token)
            }
        }
    }

    override suspend fun onGetScheduleCentral(token: ScheduleCentralBodyModel): ScheduleCentralResponseModel {
        return coroutineScope {
            makeRequest {
                RetrofitInstance().create(TicketService::class.java)
                    .getCentralAgendamento(token)
            }
        }
    }
}