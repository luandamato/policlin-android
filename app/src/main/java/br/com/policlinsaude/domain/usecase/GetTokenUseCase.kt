package br.com.policlinsaude.domain.usecase

import br.com.policlinsaude.domain.repository.Repository
import br.com.policlinsaude.domain.usecase.base.BaseRequestValues
import br.com.policlinsaude.domain.usecase.base.BaseUseCase
import io.reactivex.Flowable

class GetTokenUseCase(private val repository: Repository) :
        BaseUseCase<BaseRequestValues, String>() {

    override fun executeUseCase(requestValues: BaseRequestValues?):
            Flowable<String> {
        return repository.getToken()
    }
}
