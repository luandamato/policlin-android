package br.com.domain.usecase

import br.com.domain.exception.RequestValuesNotImplementedException
import br.com.domain.repository.Repository
import br.com.domain.usecase.base.BaseCompletableUseCase
import br.com.domain.usecase.requestvalues.EditPasswordRV
import io.reactivex.Completable

class EditPasswordUseCase(private val repository: Repository)
    : BaseCompletableUseCase<EditPasswordRV>() {

    override fun executeUseCase(requestValues: EditPasswordRV?): Completable {
        requestValues?.let {
            return repository.editPassword(requestValues.password)
        }
        return Completable.error(RequestValuesNotImplementedException(this.javaClass.canonicalName))
    }
}