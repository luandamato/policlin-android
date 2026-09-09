package br.com.policlinsaude.data.models

import br.com.policlinsaude.domain.models.Person
import br.com.policlinsaude.util.helpers.InvalidData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Converte [JsonUserResponse] (DTO da API) em [Person] (modelo de domínio).
 * Migrado/adaptado de `_legacy/.../data/datasource/networking/rest/mapper/JsonUserResponseMapper.kt`.
 */
fun JsonUserResponse.toPerson(): Person {
    val person = Person()
    person.plan.register = register ?: InvalidData.UNINITIALIZED.getString()
    person.plan.order = order ?: InvalidData.UNINITIALIZED.getString()
    person.cpf = cpf ?: InvalidData.UNINITIALIZED.getString()
    person.plan.contract = contract ?: InvalidData.UNINITIALIZED.getString()
    person.email = email ?: InvalidData.UNINITIALIZED.getString()
    person.name = name ?: InvalidData.UNINITIALIZED.getString()
    person.phone = (codeArea.orEmpty()) + (phone.orEmpty())
    person.photo = photo ?: InvalidData.UNINITIALIZED.getString()
    person.descriptionPlan = descriptionPlan ?: InvalidData.UNINITIALIZED.getString()
    person.codePlan = codePlan ?: InvalidData.UNINITIALIZED.getString()

    registrationDate?.let { person.plan.validationRegister = parseDate(it) }
    birthday?.let { person.birthday = parseDate(it) }

    return person
}

private fun parseDate(value: String): Date =
    try {
        SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault()).parse(value)
            ?: InvalidData.UNINITIALIZED.getDate()
    } catch (e: Exception) {
        InvalidData.UNINITIALIZED.getDate()
    }