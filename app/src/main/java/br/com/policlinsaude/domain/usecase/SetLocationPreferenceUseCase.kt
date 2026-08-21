package br.com.policlinsaude.domain.usecase

import br.com.policlinsaude.domain.repository.Repository
import br.com.policlinsaude.domain.usecase.base.BaseCompletableUseCase
import br.com.policlinsaude.domain.usecase.requestvalues.SetLocationPreferenceRV
import io.reactivex.Completable

/**
 * Created by lmiyagi on 3/26/18.
 */
class SetLocationPreferenceUseCase(private val repository: Repository)
    : BaseCompletableUseCase<SetLocationPreferenceRV>() {

    override fun executeUseCase(requestValues: SetLocationPreferenceRV?): Completable {
        return repository.setLocationPreference(requestValues?.value ?: false)
    }
}