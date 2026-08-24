package br.com.policlinsaude.data.models

import br.com.policlinsaude.util.helpers.InvalidData
import java.util.*

data class PresentationPerson(var name: String = InvalidData.UNINITIALIZED.getString(),
                              var cpf: String = InvalidData.UNINITIALIZED.getString(),
                              var birthday: Date = InvalidData.UNINITIALIZED.getDate(),
                              var phone: String = InvalidData.UNINITIALIZED.getString(),
                              var email: String = InvalidData.UNINITIALIZED.getString(),
                              var password: String = InvalidData.UNINITIALIZED.getString(),
                              var codePlan: String = InvalidData.UNINITIALIZED.getString(),
                              var photo: String = InvalidData.UNINITIALIZED.getString(),
                              var mothersName: String = InvalidData.UNINITIALIZED.getString(),
                              var plan: PresentationPlan = PresentationPlan())