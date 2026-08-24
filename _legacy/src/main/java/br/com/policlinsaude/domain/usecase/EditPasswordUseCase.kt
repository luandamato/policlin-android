package br.com.policlinsaude.domain.usecase

import br.com.policlinsaude.domain.exception.RequestValuesNotImplementedException
import br.com.policlinsaude.domain.repository.Repository
import br.com.policlinsaude.domain.usecase.base.BaseCompletableUseCase
import br.com.policlinsaude.domain.usecase.requestvalues.EditPasswordRV
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