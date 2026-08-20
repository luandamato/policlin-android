package br.com.domain.usecase

import br.com.domain.model.UserConnected
import br.com.domain.model.ValidateButtons
import br.com.domain.repository.Repository
import br.com.domain.usecase.base.BaseRequestValues
import br.com.domain.usecase.base.BaseUseCase
import br.com.domain.usecase.requestvalues.GetValidationUserConnectedRV
import io.reactivex.Flowable

class GetValidationButtonUseCase(private val repository: Repository)
    : BaseUseCase<BaseRequestValues, ValidateButtons>() {

    override fun executeUseCase(requestValues: BaseRequestValues?)
        : Flowable<ValidateButtons> {
            return repository.validateButtons()
        }

}