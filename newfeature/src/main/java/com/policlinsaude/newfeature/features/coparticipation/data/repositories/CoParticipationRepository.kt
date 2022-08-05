package com.policlinsaude.newfeature.features.coparticipation.data.repositories

import com.policlinsaude.newfeature.features.coparticipation.data.models.CoParticipationBodyCombo
import com.policlinsaude.newfeature.features.coparticipation.data.models.CoParticipationBodyValue
import com.policlinsaude.newfeature.features.coparticipation.data.models.CoParticipationItems
import com.policlinsaude.newfeature.features.coparticipation.data.models.CoParticipationModel

interface CoParticipationRepository {

    suspend fun onPostOptionCombo(body: CoParticipationBodyCombo): CoParticipationModel

    suspend fun onPostValuesCoParticipation(body: CoParticipationBodyValue): CoParticipationItems

}