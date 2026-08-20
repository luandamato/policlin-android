package br.com.domain.usecase

import br.com.domain.exception.RequestValuesNotImplementedException
import br.com.domain.repository.Repository
import br.com.domain.usecase.base.BaseCompletableUseCase
import br.com.domain.usecase.requestvalues.RemoveFromFavoritesRV
import io.reactivex.Completable

class RemoveFromFavoritesUseCase(private val repository: Repository)
    : BaseCompletableUseCase<RemoveFromFavoritesRV>() {

    override fun executeUseCase(requestValues: RemoveFromFavoritesRV?): Completable {
        requestValues?.let {
            return repository.removeFromFavorites(requestValues.establishment)
        }
        return Completable.error(RequestValuesNotImplementedException(this::javaClass.name))
    }
}