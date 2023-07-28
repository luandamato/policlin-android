package com.policlinsaude.newfeature.data.services

import com.policlinsaude.newfeature.data.models.UserModel
import com.policlinsaude.newfeature.features.Token.models.BeneficiariosRequestModel
import com.policlinsaude.newfeature.features.Token.models.BeneficiariosResponseModel
import com.policlinsaude.newfeature.features.Token.models.TokenBodyModel
import com.policlinsaude.newfeature.features.Token.models.TokenResponseModel
import com.policlinsaude.newfeature.features.guidAuthorizer.data.models.GuideAuthorizerRequestModel
import com.policlinsaude.newfeature.features.guidAuthorizer.data.models.GuideAuthorizerResponseModel
import com.policlinsaude.newfeature.features.incometax.data.models.IncomeTaxBodyModel
import com.policlinsaude.newfeature.features.incometax.data.models.IncomeTaxResponseModel
import com.policlinsaude.newfeature.features.incometax.data.models.ScheduleCentralBodyModel
import com.policlinsaude.newfeature.features.incometax.data.models.ScheduleCentralResponseModel
import com.policlinsaude.newfeature.features.tickets.data.models.TicketBodyModel
import com.policlinsaude.newfeature.features.tickets.data.models.TicketModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
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

    @POST("rest/apiGetDadosCentral")
    suspend fun getCentralAgendamento(
        @Body token: ScheduleCentralBodyModel
    ): Response<ScheduleCentralResponseModel>

    @POST("rest/apiTokenAtendimento")
    suspend fun getToken(
        @Body token: TokenBodyModel
    ): Response<TokenResponseModel>

    @POST("rest/apiListaBeneficiario")
    suspend fun onGetDependents(
        @Body token: BeneficiariosRequestModel
    ): Response<BeneficiariosResponseModel>
}