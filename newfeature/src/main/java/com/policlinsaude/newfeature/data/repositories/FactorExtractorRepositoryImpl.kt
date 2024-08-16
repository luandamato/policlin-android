package com.policlinsaude.newfeature.data.repositories

import com.policlinsaude.newfeature.data.models.UserModel
import com.policlinsaude.newfeature.data.networking.RetrofitInstance
import com.policlinsaude.newfeature.data.services.FactorExtractorService
import com.policlinsaude.newfeature.data.services.GuideService
import com.policlinsaude.newfeature.features.extractor.data.models.*
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

    override suspend fun onGetYearsExtractor(): FactorExtractorYearsModel {
        return coroutineScope {
            makeRequest {
                RetrofitInstance().create(FactorExtractorService::class.java)
                    .getFactorExtractorYears()
            }
        }
    }

    override suspend fun onGetMonthsExtractor(body: FactorExtractorMonthsBody): FactorExtractorMonthsModel {
        return coroutineScope {
            makeRequest {
                RetrofitInstance().create(FactorExtractorService::class.java)
                    .getFactorExtractorMonths(body)
            }
        }
    }

    override suspend fun onGetProfile(token: String, verify: Int): UserModel {
        return coroutineScope {
            makeRequest {
                RetrofitInstance().create(GuideService::class.java, baseUrl = RetrofitInstance.API_NOTIFICATION)
                    .getPerson(token, verify)
            }
        }
    }

}