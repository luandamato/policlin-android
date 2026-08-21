package br.com.data.datasource.realm.model

import br.com.policlinsaude.domain.helper.InvalidData
import io.realm.RealmObject
import java.util.*

open class RealmPlan(var register: String = InvalidData.UNINITIALIZED.getString(),
                     var order: String = InvalidData.UNINITIALIZED.getString(),
                     var contract: String = InvalidData.UNINITIALIZED.getString(),
                     var validationRegister: Date = InvalidData.UNINITIALIZED.getDate()): RealmObject()