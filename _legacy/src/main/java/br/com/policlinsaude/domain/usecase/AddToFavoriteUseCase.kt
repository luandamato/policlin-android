package br.com.policlinsaude.domain.usecase

import br.com.policlinsaude.domain.exception.RequestValuesNotImplementedException
import br.com.policlinsaude.domain.repository.Repository
import br.com.policlinsaude.domain.usecase.base.BaseCompletableUseCase
import br.com.policlinsaude.domain.usecase.requestvalues.AddToFavoriteRV
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
