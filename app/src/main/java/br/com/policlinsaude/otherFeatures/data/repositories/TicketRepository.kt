package com.policlinsaude.newfeature.data.repositories

import com.policlinsaude.newfeature.data.models.UserModel
import com.policlinsaude.newfeature.features.Token.models.BeneficiariosRequestModel
import com.policlinsaude.newfeature.features.Token.models.BeneficiariosResponseModel
import com.policlinsaude.newfeature.features.Token.models.TokenBodyModel
import com.policlinsaude.newfeature.features.Token.models.TokenResponseModel
import com.policlinsaude.newfeature.features.deleteUser.model.DeleteUserRequest
import com.policlinsaude.newfeature.features.guidAuthorizer.data.models.ComumModel
import com.policlinsaude.newfeature.features.incometax.data.models.IncomeTaxBodyModel
import com.policlinsaude.newfeature.features.incometax.data.models.IncomeTaxResponseModel
import com.policlinsaude.newfeature.features.incometax.data.models.ScheduleCentralBodyModel
import com.policlinsaude.newfeature.features.incometax.data.models.ScheduleCentralResponseModel
import com.policlinsaude.newfeature.features.tickets.data.models.TicketBodyModel
import com.policlinsaude.newfeature.features.tickets.data.models.TicketModel

interface TicketRepository {

    suspend fun onGetTickets(ticketBody: TicketBodyModel): TicketModel

    suspend fun onGetIncomeTax(token: IncomeTaxBodyModel): IncomeTaxResponseModel

    suspend fun onGetScheduleCentral(token: ScheduleCentralBodyModel): ScheduleCentralResponseModel

    suspend fun onGetToken(token: TokenBodyModel): TokenResponseModel

    suspend fun onGetDependents(token: BeneficiariosRequestModel): BeneficiariosResponseModel

    suspend fun onGetProfile(token: String, verify: Int): UserModel

    suspend fun deleteUser(token: DeleteUserRequest): ComumModel

    suspend fun logout(token: DeleteUserRequest): ComumModel

}