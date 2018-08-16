package br.com.domain.model

import br.com.domain.helper.InvalidData
import java.util.*

data class Plan(var register: String = InvalidData.UNINITIALIZED.getString(),
                var order: String = InvalidData.UNINITIALIZED.getString(),
                var contract: String = InvalidData.UNINITIALIZED.getString(),
                var validationRegister: Date = InvalidData.UNINITIALIZED.getDate())