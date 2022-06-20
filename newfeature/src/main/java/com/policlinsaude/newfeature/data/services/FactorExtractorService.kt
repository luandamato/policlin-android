package com.policlinsaude.newfeature.data.services

import com.policlinsaude.newfeature.features.extractor.data.models.FactorExtractorModel
import com.policlinsaude.newfeature.features.extractor.data.models.FactorExtractorBodyModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FactorExtractorService {

    @POST("apiapp_prot/rest/apiExtratoCoparticipacao")
    suspend fun postFactorExtractor(
        @Body body: FactorExtractorBodyModel
    ): Response<FactorExtractorModel>


}