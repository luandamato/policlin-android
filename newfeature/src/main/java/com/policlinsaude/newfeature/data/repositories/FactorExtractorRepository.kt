package com.policlinsaude.newfeature.data.repositories

import com.policlinsaude.newfeature.features.extractor.data.models.*

interface FactorExtractorRepository {

    suspend fun onPostCoParticipationExtractor(body: FactorExtractorBodyModel): FactorExtractorModel

    suspend fun onGetYearsExtractor(): FactorExtractorYearsModel

    suspend fun onGetMonthsExtractor(body: FactorExtractorMonthsBody): FactorExtractorMonthsModel

}