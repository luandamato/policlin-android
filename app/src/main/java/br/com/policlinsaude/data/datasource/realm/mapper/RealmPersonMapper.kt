package br.com.data.datasource.realm.mapper

import br.com.data.datasource.realm.model.RealmPerson
import br.com.data.datasource.realm.model.RealmPlan
import br.com.policlinsaude.domain.model.Person

object RealmPersonMapper {

    fun transform(person: Person, token: String): RealmPerson {
        val realmPerson = RealmPerson()
        realmPerson.plan?.let {
            it.register = person.plan.register
            it.order =  person.plan.order
            it.contract =  person.plan.contract
            it.validationRegister = person.plan.validationRegister
        }

        realmPerson.cpf = person.cpf
        realmPerson.email = person.email
        realmPerson.name = person.name
        realmPerson.phone = person.phone
        realmPerson.birthday = person.birthday
        realmPerson.descriptionPlan = person.descriptionPlan
        realmPerson.photo = person.photo
        realmPerson.token = token
        realmPerson.codePlan = person.codePlan

        return realmPerson
    }

    fun transform(realmPerson: RealmPerson): Person {
        val person = Person()
        realmPerson.plan?.let {
            person.plan.register = it.register
            person.plan.order =  it.order
            person.plan.contract =  it.contract
            person.plan.validationRegister = it.validationRegister
        }
        person.cpf = realmPerson.cpf
        person.email = realmPerson.email
        person.name = realmPerson.name
        person.phone = realmPerson.phone
        person.birthday = realmPerson.birthday
        person.photo = realmPerson.photo
        person.descriptionPlan = realmPerson.descriptionPlan
        person.codePlan = realmPerson.codePlan
        return person
    }
}