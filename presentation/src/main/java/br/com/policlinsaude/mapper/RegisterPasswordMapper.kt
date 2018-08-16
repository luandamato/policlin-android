package br.com.policlinsaude.mapper

import br.com.domain.model.Person
import br.com.domain.model.Plan
import br.com.policlinsaude.model.PresentationPerson
import br.com.policlinsaude.model.PresentationPlan

/**
 * Created by uziassantos on 2/4/18.
 */
object RegisterPasswordMapper {

    fun transform(presentationPerson: PresentationPerson, presentationPlan: PresentationPlan):
            Pair<Person, Plan> {
        val person = Person()
        person.password = presentationPerson.password
        person.birthday = presentationPerson.birthday
        person.email = presentationPerson.email
        person.name = presentationPerson.name
        person.cpf = presentationPerson.cpf
        person.phone = presentationPerson.phone
        person.photo = presentationPerson.photo

        val plan = transform(presentationPlan)


        return Pair(person, plan)
    }

    fun transform(presentationPlan: PresentationPlan): Plan {
        val plan = Plan()
        plan.register = presentationPlan.register
        plan.order = presentationPlan.order
        plan.contract = presentationPlan.contract
        plan.validationRegister = presentationPlan.validationRegister
        return plan
    }
}