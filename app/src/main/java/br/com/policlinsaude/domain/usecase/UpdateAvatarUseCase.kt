package br.com.policlinsaude.domain.usecase

import br.com.policlinsaude.domain.exception.RequestValuesNotImplementedException
import br.com.policlinsaude.domain.repository.Repository
import br.com.policlinsaude.domain.usecase.base.BaseCompletableUseCase
import br.com.policlinsaude.domain.usecase.requestvalues.UpdateAvatarRV
import io.reactivex.Completable

/**
 * Created by lmiyagi on 3/28/18.
 */
class UpdateAvatarUseCase(private val repository: Repository)
    : BaseCompletableUseCase<UpdateAvatarRV>() {

    override fun executeUseCase(requestValues: UpdateAvatarRV?): Completable {
        requestValues?.let {
            return repository.updateAvatar(it.imageString)
        }
        return Completable.error(RequestValuesNotImplementedException(this.javaClass.name))
    }
}