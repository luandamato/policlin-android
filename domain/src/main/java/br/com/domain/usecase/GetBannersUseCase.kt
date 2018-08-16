package br.com.domain.usecase

import br.com.domain.model.Banner
import br.com.domain.repository.Repository
import br.com.domain.usecase.base.BaseRequestValues
import br.com.domain.usecase.base.BaseUseCase
import io.reactivex.Flowable

class GetBannersUseCase(private val repository: Repository)
    : BaseUseCase<BaseRequestValues, List<Banner>>() {

    override fun executeUseCase(requestValues: BaseRequestValues?): Flowable<List<Banner>> {
        return repository.getBanners()
    }
}