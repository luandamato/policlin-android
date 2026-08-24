package com.policlinsaude.newfeature.features.coparticipation.data.repositories

import com.policlinsaude.newfeature.data.networking.RetrofitInstance
import com.policlinsaude.newfeature.data.services.FactorExtractorService
import com.policlinsaude.newfeature.features.coparticipation.data.models.CoParticipationBodyCombo
import com.policlinsaude.newfeature.features.coparticipation.data.models.CoParticipationBodyValue
import com.policlinsaude.newfeature.features.coparticipation.data.models.CoParticipationItems
import com.policlinsaude.newfeature.features.coparticipation.data.models.CoParticipationModel
import com.policlinsaude.newfeature.features.coparticipation.data.services.CoParticipationServices
import com.policlinsaude.newfeature.utils.makeRequest
import kotlinx.coroutines.coroutineScope

class CoParticipationRepositoryImpl: CoParticipationRepository {

    override suspend fun onPostOptionCombo(body: CoParticipationBodyCombo): CoParticipationModel {
        return coroutineScope {
            makeRequest {
                RetrofitInstance().create(CoParticipationServices::class.java)
                    .onPostCombos(body)
            }
        }
    }

    override suspend fun onPostValuesCoParticipation(body: CoParticipationBodyValue): CoParticipationItems {
        return coroutineScope {
            makeRequest {
                RetrofitInstance().create(CoParticipationServices::class.java)
                    .onPostValuesCoParticipation(body)
            }
        }
    }

}