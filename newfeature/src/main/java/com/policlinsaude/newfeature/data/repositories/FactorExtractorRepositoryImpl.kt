package com.policlinsaude.newfeature.data.repositories

import com.policlinsaude.newfeature.data.networking.RetrofitInstance
import com.policlinsaude.newfeature.data.services.FactorExtractorService
import com.policlinsaude.newfeature.features.extractor.data.models.FactorExtractorModel
import com.policlinsaude.newfeature.features.extractor.data.models.FactorExtractorBodyModel
import com.policlinsaude.newfeature.utils.makeRequest
import kotlinx.coroutines.coroutineScope

class FactorExtractorRepositoryImpl: FactorExtractorRepository {

    override suspend fun onPostCoParticipationExtractor(body: FactorExtractorBodyModel): FactorExtractorModel {
        return coroutineScope {
            makeRequest {
                RetrofitInstance().create(FactorExtractorService::class.java)
                    .postFactorExtractor(body)
            }
        }
    }

}