package br.com.domain.usecase

import br.com.domain.model.Person
import br.com.domain.repository.Repository
import br.com.domain.usecase.base.BaseRequestValues
import br.com.domain.usecase.base.BaseUseCase
import io.reactivex.Flowable

class GetCurrentPersonUseCase(private val repository: Repository) :
        BaseUseCase<BaseRequestValues, Person>() {

    override fun executeUseCase(requestValues: BaseRequestValues?):
            Flowable<Person> {
        return repository.getCurrentPerson()
    }
}