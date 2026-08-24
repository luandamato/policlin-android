package br.com.policlinsaude.domain.usecase

import br.com.policlinsaude.domain.model.UserConnected
import br.com.policlinsaude.domain.model.ValidateButtons
import br.com.policlinsaude.domain.repository.Repository
import br.com.policlinsaude.domain.usecase.base.BaseRequestValues
import br.com.policlinsaude.domain.usecase.base.BaseUseCase
import br.com.policlinsaude.domain.usecase.requestvalues.GetValidationUserConnectedRV
import io.reactivex.Flowable

class GetValidationButtonUseCase(private val repository: Repository)
    : BaseUseCase<BaseRequestValues, ValidateButtons>() {

    override fun executeUseCase(requestValues: BaseRequestValues?)
        : Flowable<ValidateButtons> {
            return repository.validateButtons()
        }

}