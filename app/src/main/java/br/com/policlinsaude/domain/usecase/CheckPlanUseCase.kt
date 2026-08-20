package br.com.domain.usecase

import br.com.domain.exception.RequestValuesNotImplementedException
import br.com.domain.repository.Repository
import br.com.domain.usecase.base.BaseCompletableUseCase
import br.com.domain.usecase.requestvalues.CheckPlanRV
import io.reactivex.Completable

class CheckPlanUseCase(private val repository: Repository)
    : BaseCompletableUseCase<CheckPlanRV>() {

    override fun executeUseCase(requestValues: CheckPlanRV?): Completable {
        requestValues?.let {
            return repository.checkPlan(requestValues.person, requestValues.plan)
        }
        return Completable.error(RequestValuesNotImplementedException(this.javaClass.name))
    }
}