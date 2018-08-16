package br.com.domain.model

import br.com.domain.helper.InvalidData
import java.util.*

data class Person(var name: String = InvalidData.UNINITIALIZED.getString(),
                  var cpf: String = InvalidData.UNINITIALIZED.getString(),
                  var birthday: Date = InvalidData.UNINITIALIZED.getDate(),
                  var phone: String = InvalidData.UNINITIALIZED.getString(),
                  var email: String = InvalidData.UNINITIALIZED.getString(),
                  var password: String = InvalidData.UNINITIALIZED.getString(),
                  var descriptionPlan: String = InvalidData.UNINITIALIZED.getString(),
                  var photo: String = InvalidData.UNINITIALIZED.getString(),
                  var codePlan: String = InvalidData.UNINITIALIZED.getString(),
                  var plan: Plan = Plan()) {

    fun getCodeArea(): String {
        if (phone.length < 2) return ""
        return phone.substring(0, 2)
    }

    fun getPhoneWithoutCodeArea(): String {
        if (phone.length < 2) return ""
        return phone.substring(2)
    }
}