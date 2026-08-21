package br.com.policlinsaude.model

import br.com.policlinsaude.domain.helper.InvalidData
import java.util.*

data class PresentationPlan(var register: String = InvalidData.UNINITIALIZED.getString(),
                            var order: String = InvalidData.UNINITIALIZED.getString(),
                            var contract: String = InvalidData.UNINITIALIZED.getString(),
                            var validationRegister: Date = InvalidData.UNINITIALIZED.getDate())