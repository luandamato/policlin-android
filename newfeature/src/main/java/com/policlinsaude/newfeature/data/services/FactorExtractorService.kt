package com.policlinsaude.newfeature.data.services

import com.policlinsaude.newfeature.features.extractor.data.models.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FactorExtractorService {

    @POST("rest/apiExtratoCoparticipacao")
    suspend fun postFactorExtractor(
        @Body body: FactorExtractorBodyModel
    ): Response<FactorExtractorModel>

    @POST("rest/apiExtratoCopartComboAno")
    suspend fun getFactorExtractorYears(): Response<FactorExtractorYearsModel>

    @POST("rest/apiExtratoCopartComboMes")
    suspend fun getFactorExtractorMonths(@Body body: FactorExtractorMonthsBody): Response<FactorExtractorMonthsModel>


}