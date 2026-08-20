package br.com.policlinsaude.domain.usecase.base

import io.reactivex.Completable

/**
 * Created by lmiyagi on 3/26/18.
 */
abstract class BaseCompletableUseCase<in RV : BaseRequestValues> {

    private var requestValue: RV? = null

    fun setRequestValues(requestValues: RV?) {
        this.requestValue = requestValues
    }

    fun run(): Completable {
        return executeUseCase(requestValue)
    }

    abstract fun executeUseCase(requestValues: RV? = null): Completable
}
