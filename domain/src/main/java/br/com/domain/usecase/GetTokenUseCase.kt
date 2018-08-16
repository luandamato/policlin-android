package br.com.domain.usecase

import br.com.domain.repository.Repository
import br.com.domain.usecase.base.BaseRequestValues
import br.com.domain.usecase.base.BaseUseCase
import io.reactivex.Flowable

class GetTokenUseCase(private val repository: Repository) :
        BaseUseCase<BaseRequestValues, String>() {

    override fun executeUseCase(requestValues: BaseRequestValues?):
            Flowable<String> {
        return repository.getToken()
    }
}