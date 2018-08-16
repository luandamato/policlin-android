package br.com.domain.usecase

import br.com.domain.exception.RequestValuesNotImplementedException
import br.com.domain.repository.Repository
import br.com.domain.usecase.base.BaseUseCase
import br.com.domain.usecase.requestvalues.DoLoginRV
import io.reactivex.Flowable

class DoLoginUseCase(private val repository: Repository) :
        BaseUseCase<DoLoginRV, Any>() {

    override fun executeUseCase(requestValues: DoLoginRV?):
            Flowable<Any> {
        requestValues?.let {
            return repository.doLogin(register = requestValues.register,
                    order = requestValues.order,
                    password = requestValues.password).toFlowable()
        }
        return Flowable.error(RequestValuesNotImplementedException(this.javaClass.name))
    }
}