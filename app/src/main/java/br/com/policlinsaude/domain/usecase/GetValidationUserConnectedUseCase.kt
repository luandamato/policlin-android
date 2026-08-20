package br.com.domain.usecase

import br.com.domain.model.UserConnected
import br.com.domain.repository.Repository
import br.com.domain.usecase.base.BaseUseCase
import br.com.domain.usecase.requestvalues.GetValidationUserConnectedRV
import io.reactivex.Flowable

class GetValidationUserConnectedUseCase(private val repository: Repository)
    : BaseUseCase<GetValidationUserConnectedRV, UserConnected>() {

    override fun executeUseCase(requestValues: GetValidationUserConnectedRV?): Flowable<UserConnected> {
            return if (requestValues == null || requestValues.registration.isEmpty()) {
                Flowable.error(Throwable("Registration must not be null"))
            } else {
                return repository.validateUserConnected(requestValues.registration, requestValues.order)
            }

    }
}