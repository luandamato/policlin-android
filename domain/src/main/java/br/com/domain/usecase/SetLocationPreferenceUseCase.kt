package br.com.domain.usecase

import br.com.domain.repository.Repository
import br.com.domain.usecase.base.BaseCompletableUseCase
import br.com.domain.usecase.requestvalues.SetLocationPreferenceRV
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