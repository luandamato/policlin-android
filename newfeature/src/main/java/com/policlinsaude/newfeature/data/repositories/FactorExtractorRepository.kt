package com.policlinsaude.newfeature.data.repositories

import com.policlinsaude.newfeature.features.extractor.data.models.FactorExtractorModel
import com.policlinsaude.newfeature.features.extractor.data.models.FactorExtractorBodyModel

interface FactorExtractorRepository {

    suspend fun onPostCoParticipationExtractor(body: FactorExtractorBodyModel): FactorExtractorModel

}