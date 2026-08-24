package br.com.policlinsaude.domain.usecase

import br.com.policlinsaude.domain.model.UnitsList
import br.com.policlinsaude.domain.repository.Repository
import br.com.policlinsaude.domain.usecase.base.BaseRequestValues
import br.com.policlinsaude.domain.usecase.base.BaseUseCase
import io.reactivex.Flowable

class GetUnitsUseCase(private val repository: Repository) :
        BaseUseCase<BaseRequestValues, UnitsList>() {

    override fun executeUseCase(requestValues: BaseRequestValues?):
            Flowable<UnitsList> {
          return repository.getUnits()
    }
}