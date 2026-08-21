package br.com.policlinsaude.domain.usecase

import br.com.policlinsaude.domain.model.Banner
import br.com.policlinsaude.domain.repository.Repository
import br.com.policlinsaude.domain.usecase.base.BaseRequestValues
import br.com.policlinsaude.domain.usecase.base.BaseUseCase
import io.reactivex.Flowable

class GetBannersUseCase(private val repository: Repository)
    : BaseUseCase<BaseRequestValues, List<Banner>>() {

    override fun executeUseCase(requestValues: BaseRequestValues?): Flowable<List<Banner>> {
        return repository.getBanners()
    }
}