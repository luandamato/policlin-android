package br.com.policlinsaude.domain.usecase

import br.com.policlinsaude.domain.model.Establishment
import br.com.policlinsaude.domain.repository.Repository
import br.com.policlinsaude.domain.usecase.base.BaseRequestValues
import br.com.policlinsaude.domain.usecase.base.BaseUseCase
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