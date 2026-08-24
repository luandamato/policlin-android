package br.com.policlinsaude.data.datasource.realm.model

import br.com.policlinsaude.domain.helper.InvalidData
import java.util.*

data class RealmPlan(
    var register: String = InvalidData.UNINITIALIZED.getString(),
    var order: String = InvalidData.UNINITIALIZED.getString(),
    var contract: String = InvalidData.UNINITIALIZED.getString(),
    var validationRegister: Date = InvalidData.UNINITIALIZED.getDate()
)
