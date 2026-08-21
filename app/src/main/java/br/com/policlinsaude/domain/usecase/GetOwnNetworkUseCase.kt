package br.com.policlinsaude.domain.usecase

import br.com.policlinsaude.domain.model.OwnNetworkList
import br.com.policlinsaude.domain.repository.Repository
import br.com.policlinsaude.domain.usecase.base.BaseRequestValues
import br.com.policlinsaude.domain.usecase.base.BaseUseCase
import io.reactivex.Flowable

class GetOwnNetworkUseCase(private val repository: Repository) :
        BaseUseCase<BaseRequestValues, OwnNetworkList>() {

    override fun executeUseCase(requestValues: BaseRequestValues?):
            Flowable<OwnNetworkList> {
        return repository.getOwnNetwork()
    }
}