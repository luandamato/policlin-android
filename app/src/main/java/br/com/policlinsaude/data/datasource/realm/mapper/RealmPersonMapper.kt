package br.com.policlinsaude.data.datasource.realm.mapper

import br.com.policlinsaude.data.datasource.realm.model.RealmPerson
import br.com.policlinsaude.data.datasource.realm.model.RealmPlan
import br.com.policlinsaude.domain.model.Person
import br.com.policlinsaude.domain.model.Plan

object RealmPersonMapper {

    fun transform(person: Person, token: String): RealmPerson {
        return RealmPerson(
            name = person.name,
            cpf = person.cpf,
            birthday = person.birthday,
            phone = person.phone,
            email = person.email,
            password = person.password,
            descriptionPlan = person.descriptionPlan,
            photo = person.photo,
            codePlan = person.codePlan,
            token = token,
            plan = RealmPlan(
                register = person.plan.register,
                order = person.plan.order,
                contract = person.plan.contract,
                validationRegister = person.plan.validationRegister
            )
        )
    }

    fun transform(realmPerson: RealmPerson): Person {
        val person = Person()
        realmPerson.plan?.let {
            person.plan = Plan(
                register = it.register,
                order = it.order,
                contract = it.contract,
                validationRegister = it.validationRegister
            )
        }
        person.cpf = realmPerson.cpf
        person.email = realmPerson.email
        person.name = realmPerson.name
        person.phone = realmPerson.phone
        person.birthday = realmPerson.birthday
        person.photo = realmPerson.photo
        person.descriptionPlan = realmPerson.descriptionPlan
        person.codePlan = realmPerson.codePlan
        person.password = realmPerson.password
        return person
    }
}
