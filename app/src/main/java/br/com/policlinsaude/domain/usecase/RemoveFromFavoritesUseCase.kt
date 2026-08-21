package br.com.policlinsaude.domain.usecase

import br.com.policlinsaude.domain.exception.RequestValuesNotImplementedException
import br.com.policlinsaude.domain.repository.Repository
import br.com.policlinsaude.domain.usecase.base.BaseCompletableUseCase
import br.com.policlinsaude.domain.usecase.requestvalues.RemoveFromFavoritesRV
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