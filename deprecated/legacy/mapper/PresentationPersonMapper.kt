package br.com.policlinsaude.ui.legacy.mapper

import br.com.domain.model.Person
import br.com.policlinsaude.model.PresentationPerson

object PresentationPersonMapper {
    fun transform(person: Person): PresentationPerson {
        val presentationPerson = PresentationPerson()
        presentationPerson.plan.register = person.plan.register
        presentationPerson.plan.order = person.plan.order
        presentationPerson.plan.contract = person.plan.contract
        presentationPerson.plan.validationRegister = person.plan.validationRegister
        presentationPerson.cpf = person.cpf
        presentationPerson.email = person.email
        presentationPerson.name = person.name
        presentationPerson.phone = person.phone
        presentationPerson.birthday = person.birthday
        presentationPerson.codePlan = person.codePlan
        presentationPerson.photo = person.photo
        presentationPerson.mothersName = person.mothersName
        return presentationPerson
    }

    fun transform(presentationPerson: PresentationPerson): Person {
        val person = Person()
        person.plan.register = presentationPerson.plan.register
        person.plan.order = presentationPerson.plan.order
        person.plan.contract = presentationPerson.plan.contract
        person.plan.validationRegister = presentationPerson.plan.validationRegister
        person.cpf = presentationPerson.cpf
        person.email = presentationPerson.email
        person.name = presentationPerson.name
        person.phone = presentationPerson.phone
        person.birthday = presentationPerson.birthday
        person.codePlan = presentationPerson.codePlan
        person.photo = presentationPerson.photo
        person.mothersName = presentationPerson.mothersName
        return person
    }
}