package br.com.domain.usecase

import br.com.domain.model.HealthInsurancePhotoList
import br.com.domain.model.MedicalGuideOptions
import br.com.domain.repository.Repository
import br.com.domain.usecase.base.BaseRequestValues
import br.com.domain.usecase.base.BaseUseCase
import io.reactivex.Flowable

class GetHealthInsurancePhotoUseCase(private val repository: Repository) :
        //BaseUseCase<BaseRequestValues, String>() {
        BaseUseCase<BaseRequestValues, HealthInsurancePhotoList>() {

    override fun executeUseCase(requestValues: BaseRequestValues?):
        //    Flowable<String> {
            Flowable<HealthInsurancePhotoList> {
        return repository.getHealthInsurancePhoto()
    }
}
