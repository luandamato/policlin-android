package br.com.data.datasource.realm.model

import br.com.domain.helper.InvalidData
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class RealmPerson(var name: String = InvalidData.UNINITIALIZED.getString(),
                       var cpf: String = InvalidData.UNINITIALIZED.getString(),
                       var birthday: Date = InvalidData.UNINITIALIZED.getDate(),
                       var phone: String = InvalidData.UNINITIALIZED.getString(),
                       var email: String = InvalidData.UNINITIALIZED.getString(),
                       var password: String = InvalidData.UNINITIALIZED.getString(),
                       var descriptionPlan: String = InvalidData.UNINITIALIZED.getString(),
                       var photo: String = InvalidData.UNINITIALIZED.getString(),
                       var codePlan: String = InvalidData.UNINITIALIZED.getString(),
                       @PrimaryKey var token: String = InvalidData.UNINITIALIZED.getString(),
                       var plan: RealmPlan? = RealmPlan()): RealmObject()