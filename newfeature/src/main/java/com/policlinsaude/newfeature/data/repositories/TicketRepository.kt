package com.policlinsaude.newfeature.data.repositories

import com.policlinsaude.newfeature.data.models.UserModel
import com.policlinsaude.newfeature.features.guidAuthorizer.data.models.GuideAuthorizerRequestModel
import com.policlinsaude.newfeature.features.guidAuthorizer.data.models.GuideAuthorizerResponseModel
import com.policlinsaude.newfeature.features.incometax.data.models.IncomeTaxBodyModel
import com.policlinsaude.newfeature.features.incometax.data.models.IncomeTaxResponseModel
import com.policlinsaude.newfeature.features.scheduleCentral.models.ScheduleCentralBodyModel
import com.policlinsaude.newfeature.features.scheduleCentral.models.ScheduleCentralResponseModel
import com.policlinsaude.newfeature.features.tickets.data.models.TicketBodyModel
import com.policlinsaude.newfeature.features.tickets.data.models.TicketModel

interface TicketRepository {

    suspend fun onGetTickets(ticketBody: TicketBodyModel): TicketModel

    suspend fun onGetIncomeTax(token: IncomeTaxBodyModel): IncomeTaxResponseModel

    suspend fun onGetScheduleCentral(token: ScheduleCentralBodyModel): ScheduleCentralResponseModel

}