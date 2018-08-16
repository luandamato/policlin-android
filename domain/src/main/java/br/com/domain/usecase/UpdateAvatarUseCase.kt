package br.com.domain.usecase

import br.com.domain.exception.RequestValuesNotImplementedException
import br.com.domain.repository.Repository
import br.com.domain.usecase.base.BaseCompletableUseCase
import br.com.domain.usecase.requestvalues.UpdateAvatarRV
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