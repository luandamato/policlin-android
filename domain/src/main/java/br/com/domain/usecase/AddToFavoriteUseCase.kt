package br.com.domain.usecase

import br.com.domain.exception.RequestValuesNotImplementedException
import br.com.domain.repository.Repository
import br.com.domain.usecase.base.BaseCompletableUseCase
import br.com.domain.usecase.requestvalues.AddToFavoriteRV
import io.reactivex.Completable

/**
 * Created by lmiyagi on 3/27/18.
 */
class AddToFavoriteUseCase(private val repository: Repository) :
        BaseCompletableUseCase<AddToFavoriteRV>() {

    override fun executeUseCase(requestValues: AddToFavoriteRV?): Completable {
        requestValues?.let {
            return repository.addToFavorites(requestValues.establishment)
        }
        return Completable.error(RequestValuesNotImplementedException(this.javaClass.name))
    }
}