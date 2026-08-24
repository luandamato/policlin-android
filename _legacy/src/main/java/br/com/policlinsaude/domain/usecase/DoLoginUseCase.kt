package br.com.policlinsaude.domain.usecase

import br.com.policlinsaude.domain.exception.RequestValuesNotImplementedException
import br.com.policlinsaude.domain.repository.Repository
import br.com.policlinsaude.domain.usecase.base.BaseUseCase
import br.com.policlinsaude.domain.usecase.requestvalues.DoLoginRV
import io.reactivex.Flowable

class DoLoginUseCase(private val repository: Repository) :
        BaseUseCase<DoLoginRV, Any>() {

    override fun executeUseCase(requestValues: DoLoginRV?):
            Flowable<Any> {
        requestValues?.let {
            return repository.doLogin(
                register = requestValues.register,
                order = requestValues.order,
                password = requestValues.password,
                firebaseToken = requestValues.firebaseToken,
                osVersion = requestValues.osVersion
            ).toFlowable()
        }
        return Flowable.error(RequestValuesNotImplementedException(this.javaClass.name))
    }
}
