package br.com.policlinsaude.data.models

import br.com.policlinsaude.util.helpers.InvalidData
import java.util.*

data class PresentationPlan(var register: String = InvalidData.UNINITIALIZED.getString(),
                            var order: String = InvalidData.UNINITIALIZED.getString(),
                            var contract: String = InvalidData.UNINITIALIZED.getString(),
                            var validationRegister: Date = InvalidData.UNINITIALIZED.getDate())