package com.policlinsaude.newfeature.features.coparticipation.data.services

import com.policlinsaude.newfeature.features.coparticipation.data.models.CoParticipationBodyCombo
import com.policlinsaude.newfeature.features.coparticipation.data.models.CoParticipationBodyValue
import com.policlinsaude.newfeature.features.coparticipation.data.models.CoParticipationItems
import com.policlinsaude.newfeature.features.coparticipation.data.models.CoParticipationModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CoParticipationServices {

    @POST("rest/APIValoresCopartCombos")
    suspend fun onPostCombos(@Body body: CoParticipationBodyCombo): Response<CoParticipationModel>

    @POST("rest/apiValoresCopart")
    suspend fun onPostValuesCoParticipation(@Body body: CoParticipationBodyValue): Response<CoParticipationItems>

}