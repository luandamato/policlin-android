package br.com.policlinsaude.model

import br.com.domain.helper.InvalidData
import java.util.*

data class PresentationPerson(var name: String = InvalidData.UNINITIALIZED.getString(),
                              var cpf: String = InvalidData.UNINITIALIZED.getString(),
                              var birthday: Date = InvalidData.UNINITIALIZED.getDate(),
                              var phone: String = InvalidData.UNINITIALIZED.getString(),
                              var email: String = InvalidData.UNINITIALIZED.getString(),
                              var password: String = InvalidData.UNINITIALIZED.getString(),
                              var codePlan: String = InvalidData.UNINITIALIZED.getString(),
                              var photo: String = InvalidData.UNINITIALIZED.getString(),
                              var plan: PresentationPlan = PresentationPlan())