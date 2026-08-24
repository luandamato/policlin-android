package br.com.policlinsaude.domain.usecase

import br.com.policlinsaude.domain.exception.RequestValuesNotImplementedException
import br.com.policlinsaude.domain.model.MedicalGuidePlan
import br.com.policlinsaude.domain.repository.Repository
import br.com.policlinsaude.domain.usecase.base.BaseUseCase
import br.com.policlinsaude.domain.usecase.requestvalues.GetPlansRV
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