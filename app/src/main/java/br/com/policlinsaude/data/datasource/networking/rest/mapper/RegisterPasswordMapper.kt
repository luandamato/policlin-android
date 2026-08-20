package br.com.data.datasource.networking.rest.mapper

import br.com.data.datasource.networking.rest.model.JsonRegisterPasswordBody
import br.com.data.helper.DateHelper
import br.com.domain.model.Person
import br.com.domain.model.Plan

/**
 * Created by uziassantos on 2/4/18.
 */
object RegisterPasswordMapper {

    fun transform(person: Person, plan: Plan): JsonRegisterPasswordBody {
        val json = JsonRegisterPasswordBody()
        json.register = plan.register
        json.order = plan.order
        json.cpf = person.cpf
        json.contract = plan.contract
        json.email = person.email
        json.name = person.name.toUpperCase()
        json.codeAreaPhone = person.getCodeArea()
        json.phone = person.getPhoneWithoutCodeArea()
        json.validationRegistration = DateHelper.getStringFromDate(DateHelper.FORMAT_DATE_YYYYMMDD, plan.validationRegister)
        json.password = person.password
        json.acceptTerm = 1
        json.birthday = DateHelper.getStringFromDate(DateHelper.FORMAT_DATE_YYYYMMDD, person.birthday)
        json.avatar = person.photo
        json.mothersName = person.mothersName
        return json
    }
}