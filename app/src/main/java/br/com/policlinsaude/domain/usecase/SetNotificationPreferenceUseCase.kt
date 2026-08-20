package br.com.domain.usecase

import br.com.domain.repository.Repository
import br.com.domain.usecase.base.BaseCompletableUseCase
import br.com.domain.usecase.requestvalues.SetNotificationPreferenceRV
import io.reactivex.Completable

/**
 * Created by lmiyagi on 3/26/18.
 */
class SetNotificationPreferenceUseCase(private val repository: Repository)
    : BaseCompletableUseCase<SetNotificationPreferenceRV>() {

    override fun executeUseCase(requestValues: SetNotificationPreferenceRV?): Completable {
        return repository.setNotificationPreference(requestValues?.value ?: false)
    }
}