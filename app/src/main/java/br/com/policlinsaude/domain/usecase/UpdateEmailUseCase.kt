package br.com.domain.usecase

import br.com.domain.exception.RequestValuesNotImplementedException
import br.com.domain.repository.Repository
import br.com.domain.usecase.base.BaseCompletableUseCase
import br.com.domain.usecase.requestvalues.UpdatePhoneRV
import io.reactivex.Completable

class UpdateEmailUseCase(private val repository: Repository)
    : BaseCompletableUseCase<UpdatePhoneRV>() {

    override fun executeUseCase(requestValues: UpdatePhoneRV?): Completable {
        requestValues?.let {
            return repository.updatePhone(it.codeArea, it.phone)
        }
        return Completable.error(RequestValuesNotImplementedException(this.javaClass.name))
    }
}