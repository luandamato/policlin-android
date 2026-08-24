package br.com.policlinsaude.domain.usecase

import br.com.policlinsaude.domain.repository.Repository
import br.com.policlinsaude.domain.usecase.base.BaseUseCase
import br.com.policlinsaude.domain.usecase.requestvalues.GetBooleanPreferenceRV
import io.reactivex.Flowable

/**
 * Created by lmiyagi on 3/26/18.
 */
class GetBooleanPreferenceUseCase(private val repository: Repository)
    : BaseUseCase<GetBooleanPreferenceRV, Boolean>() {

    override fun executeUseCase(requestValues: GetBooleanPreferenceRV?): Flowable<Boolean> {
        return if (requestValues == null || requestValues.key.isEmpty()) {
            Flowable.error(Throwable("Key must not be null"))
        } else {
            repository.getBooleanPreference(requestValues.key, requestValues.defaultValue)
        }
    }
}