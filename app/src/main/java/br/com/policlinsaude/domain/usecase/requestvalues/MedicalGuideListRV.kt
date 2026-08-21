package br.com.policlinsaude.domain.usecase.requestvalues

import br.com.policlinsaude.domain.helper.InvalidData
import br.com.policlinsaude.domain.model.Person
import br.com.policlinsaude.domain.model.Plan
import br.com.policlinsaude.domain.usecase.base.BaseRequestValues

data class MedicalGuideListRV(val codePlan: Int = InvalidData.UNINITIALIZED.getInt(),
                              val codeCity: Int = InvalidData.UNINITIALIZED.getInt(),
                              val codeSpecialityService: Int = InvalidData.UNINITIALIZED.getInt(),
                              val ownNetwork: Int = InvalidData.UNINITIALIZED.getInt(),
                              val latitude: Double = InvalidData.UNINITIALIZED.getDouble(),
                              val longitude: Double = InvalidData.UNINITIALIZED.getDouble(),
                              val codeProfessionalClass: String = InvalidData.UNINITIALIZED.getString(), //Andre
                              val codeServiceType: String = InvalidData.UNINITIALIZED.getString(),//Andre
                              val codeEstablishmentType: String = InvalidData.UNINITIALIZED.getString(),//Andre
                              val specialityType: String = InvalidData.UNINITIALIZED.getString(),
                              val address_filter: String = InvalidData.UNINITIALIZED.getString(),
                              val neighborhood_filter: String = InvalidData.UNINITIALIZED.getString(),
                              val zipcode_filter: String = InvalidData.UNINITIALIZED.getString(),
                              val number_on_the_board_filter: String = InvalidData.UNINITIALIZED.getString(),
                              val prof_fantasy_filter: String = InvalidData.UNINITIALIZED.getString(),
                              val cnpj_filter: String = InvalidData.UNINITIALIZED.getString(),
                              val phones_filter: String = InvalidData.UNINITIALIZED.getString(),
                              val qualificationsSearch: String? = InvalidData.UNINITIALIZED.getString())//Andre
    : BaseRequestValues