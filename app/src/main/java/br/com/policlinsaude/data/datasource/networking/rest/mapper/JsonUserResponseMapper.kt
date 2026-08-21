package br.com.data.datasource.networking.rest.mapper

import br.com.data.datasource.networking.rest.model.JsonUserResponse
import br.com.data.helper.DateHelper
import br.com.data.helper.InvalidData
import br.com.policlinsaude.domain.model.Person

object JsonUserResponseMapper {

    fun transform(jsonUserResponse: JsonUserResponse): Person {
        val person = Person()
        person.plan.register = jsonUserResponse.register ?: InvalidData.UNINITIALIZED.getString()
        person.plan.order = jsonUserResponse.order ?: InvalidData.UNINITIALIZED.getString()
        person.cpf = jsonUserResponse.cpf ?: InvalidData.UNINITIALIZED.getString()
        person.plan.contract = jsonUserResponse.contract ?: InvalidData.UNINITIALIZED.getString()
        person.email = jsonUserResponse.email ?: InvalidData.UNINITIALIZED.getString()
        person.name = jsonUserResponse.name ?: InvalidData.UNINITIALIZED.getString()
        person.phone = jsonUserResponse.codeArea ?: InvalidData.UNINITIALIZED.getString()
        person.phone += jsonUserResponse.phone ?: InvalidData.UNINITIALIZED.getString()
        person.photo = jsonUserResponse.photo ?: InvalidData.UNINITIALIZED.getString()
        person.descriptionPlan = jsonUserResponse.descriptionPlan ?: InvalidData.UNINITIALIZED.getString()
        person.codePlan = jsonUserResponse.codePlan ?: InvalidData.UNINITIALIZED.getString()

        jsonUserResponse.registrationDate?.let {
            person.plan.validationRegister = DateHelper.getDate(DateHelper.FORMAT_DATE_DDMMYYYYHHMMSS,
                    it)
        }

        jsonUserResponse.birthday?.let {
            person.birthday = DateHelper.getDate(DateHelper.FORMAT_DATE_DDMMYYYYHHMMSS,
                    it)
        }
        return person
    }
}