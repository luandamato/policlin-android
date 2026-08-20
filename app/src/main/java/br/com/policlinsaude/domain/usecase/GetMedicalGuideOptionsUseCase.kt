package br.com.domain.usecase

import br.com.domain.model.MedicalGuideOptions
import br.com.domain.repository.Repository
import br.com.domain.usecase.base.BaseRequestValues
import br.com.domain.usecase.base.BaseUseCase
import io.reactivex.Flowable

class GetMedicalGuideOptionsUseCase(private val repository: Repository) :
        BaseUseCase<BaseRequestValues, MedicalGuideOptions>() {

    override fun executeUseCase(requestValues: BaseRequestValues?):
            Flowable<MedicalGuideOptions> {
        return repository.getMedicalGuideOptions()
    }
}