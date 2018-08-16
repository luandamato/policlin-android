package br.com.domain.usecase

import br.com.domain.model.OwnNetworkList
import br.com.domain.repository.Repository
import br.com.domain.usecase.base.BaseRequestValues
import br.com.domain.usecase.base.BaseUseCase
import io.reactivex.Flowable

class GetOwnNetworkUseCase(private val repository: Repository) :
        BaseUseCase<BaseRequestValues, OwnNetworkList>() {

    override fun executeUseCase(requestValues: BaseRequestValues?):
            Flowable<OwnNetworkList> {
        return repository.getOwnNetwork()
    }
}