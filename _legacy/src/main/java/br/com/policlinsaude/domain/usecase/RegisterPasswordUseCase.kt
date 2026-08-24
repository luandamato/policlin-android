package br.com.policlinsaude.domain.usecase

import br.com.policlinsaude.domain.exception.RequestValuesNotImplementedException
import br.com.policlinsaude.domain.repository.Repository
import br.com.policlinsaude.domain.usecase.base.BaseUseCase
import br.com.policlinsaude.domain.usecase.requestvalues.RegisterPasswordRV
import io.reactivex.Flowable

class RegisterPasswordUseCase(private val repository: Repository) :
        BaseUseCase<RegisterPasswordRV, String>() {

    override fun executeUseCase(requestValues: RegisterPasswordRV?):
            Flowable<String> {
        requestValues?.let {
            return repository.registerPassword(person = it.person, plan = it.plan)
        }
        return Flowable.error(RequestValuesNotImplementedException(this.javaClass.name))
    }
}