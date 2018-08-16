package br.com.domain.usecase

import br.com.domain.exception.RequestValuesNotImplementedException
import br.com.domain.repository.Repository
import br.com.domain.usecase.base.BaseUseCase
import br.com.domain.usecase.requestvalues.RecoverPasswordRV
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