package br.com.domain.usecase

import br.com.domain.model.UnitsList
import br.com.domain.repository.Repository
import br.com.domain.usecase.base.BaseRequestValues
import br.com.domain.usecase.base.BaseUseCase
import io.reactivex.Flowable

class GetUnitsUseCase(private val repository: Repository) :
        BaseUseCase<BaseRequestValues, UnitsList>() {

    override fun executeUseCase(requestValues: BaseRequestValues?):
            Flowable<UnitsList> {
          return repository.getUnits()
    }
}