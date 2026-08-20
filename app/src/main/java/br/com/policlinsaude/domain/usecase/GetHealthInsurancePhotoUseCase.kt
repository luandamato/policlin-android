package br.com.policlinsaude.domain.usecase

import br.com.policlinsaude.domain.model.HealthInsurancePhotoList
import br.com.policlinsaude.domain.repository.Repository
import br.com.policlinsaude.domain.usecase.base.BaseRequestValues
import br.com.policlinsaude.domain.usecase.base.BaseUseCase
import io.reactivex.Flowable

class GetHealthInsurancePhotoUseCase(private val repository: Repository) :
        BaseUseCase<BaseRequestValues, HealthInsurancePhotoList>() {

    override fun executeUseCase(requestValues: BaseRequestValues?):
            Flowable<HealthInsurancePhotoList> {
        return repository.getHealthInsurancePhoto()
    }
}
