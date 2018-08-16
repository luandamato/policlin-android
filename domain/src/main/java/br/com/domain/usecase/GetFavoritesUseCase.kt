package br.com.domain.usecase

import br.com.domain.model.Establishment
import br.com.domain.repository.Repository
import br.com.domain.usecase.base.BaseRequestValues
import br.com.domain.usecase.base.BaseUseCase
import io.reactivex.Flowable

/**
 * Created by lmiyagi on 3/27/18.
 */
class GetFavoritesUseCase(private val repository: Repository)
    : BaseUseCase<BaseRequestValues, List<Establishment>>() {

    override fun executeUseCase(requestValues: BaseRequestValues?): Flowable<List<Establishment>> {
        return repository.getFavorites()
    }
}