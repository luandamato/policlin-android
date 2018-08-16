package br.com.domain.usecase

import br.com.domain.exception.RequestValuesNotImplementedException
import br.com.domain.model.MedicalGuidePlan
import br.com.domain.repository.Repository
import br.com.domain.usecase.base.BaseUseCase
import br.com.domain.usecase.requestvalues.GetPlansRV
import io.reactivex.Flowable

/**
 * Created by lmiyagi on 3/21/18.
 */
class GetPlansUseCase(private val repository: Repository) :
        BaseUseCase<GetPlansRV, List<MedicalGuidePlan>>() {

    override fun executeUseCase(requestValues: GetPlansRV?): Flowable<List<MedicalGuidePlan>> {
        requestValues?.let {
            return repository.getMedicalGuidePlans(proUF = requestValues.proUF,
                    prsCod = requestValues.prsCod,
                    proCls = requestValues.proCls,
                    proCod = requestValues.proCod)
        }
        return Flowable.error(RequestValuesNotImplementedException(this.javaClass.name))
    }
}