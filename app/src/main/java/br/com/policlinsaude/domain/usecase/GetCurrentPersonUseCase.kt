package br.com.policlinsaude.domain.usecase

import br.com.policlinsaude.domain.model.Person
import br.com.policlinsaude.domain.repository.Repository
import br.com.policlinsaude.domain.usecase.base.BaseRequestValues
import br.com.policlinsaude.domain.usecase.base.BaseUseCase
import io.reactivex.Flowable

class GetCurrentPersonUseCase(private val repository: Repository) :
        BaseUseCase<BaseRequestValues, Person>() {

    override fun executeUseCase(requestValues: BaseRequestValues?):
            Flowable<Person> {
        return repository.getCurrentPerson()
    }
}