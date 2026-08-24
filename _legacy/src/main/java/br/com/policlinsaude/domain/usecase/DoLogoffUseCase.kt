package br.com.policlinsaude.domain.usecase

import br.com.policlinsaude.domain.repository.Repository
import br.com.policlinsaude.domain.usecase.base.BaseCompletableUseCase
import br.com.policlinsaude.domain.usecase.base.BaseRequestValues
import io.reactivex.Completable

/**
 * Created by lmiyagi on 3/26/18.
 */
class DoLogoffUseCase(private val repository: Repository)
    : BaseCompletableUseCase<BaseRequestValues>() {

    override fun executeUseCase(requestValues: BaseRequestValues?): Completable {
        return repository.doLogoff()
    }
}