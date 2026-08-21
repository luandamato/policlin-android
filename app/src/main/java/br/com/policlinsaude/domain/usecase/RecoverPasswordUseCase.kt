package br.com.policlinsaude.domain.usecase

import br.com.policlinsaude.domain.exception.RequestValuesNotImplementedException
import br.com.policlinsaude.domain.repository.Repository
import br.com.policlinsaude.domain.usecase.base.BaseUseCase
import br.com.policlinsaude.domain.usecase.requestvalues.RecoverPasswordRV
import io.reactivex.Flowable

class RecoverPasswordUseCase(private val repository: Repository) :
        BaseUseCase<RecoverPasswordRV, Any>() {

    override fun executeUseCase(requestValues: RecoverPasswordRV?):
            Flowable<Any> {
        requestValues?.let {
            return repository.recoverPassword(register = requestValues.register,
                    order = requestValues.order,
                    email = requestValues.email).toFlowable()
        }
        return Flowable.error(RequestValuesNotImplementedException(this.javaClass.name))
    }
}