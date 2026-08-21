package br.com.policlinsaude.domain.usecase

import br.com.policlinsaude.domain.model.MedicalGuideOptions
import br.com.policlinsaude.domain.repository.Repository
import br.com.policlinsaude.domain.usecase.base.BaseRequestValues
import br.com.policlinsaude.domain.usecase.base.BaseUseCase
import io.reactivex.Flowable

class GetMedicalGuideOptionsUseCase(private val repository: Repository) :
        BaseUseCase<BaseRequestValues, MedicalGuideOptions>() {

    override fun executeUseCase(requestValues: BaseRequestValues?):
            Flowable<MedicalGuideOptions> {
        return repository.getMedicalGuideOptions()
    }
}