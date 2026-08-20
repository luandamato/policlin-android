package br.com.policlinsaude.core.helper

import android.util.Log
import br.com.domain.usecase.base.BaseCompletableUseCase
import br.com.domain.usecase.base.BaseRequestValues
import br.com.domain.usecase.base.BaseUseCase
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object UseCaseHandler {

    fun <RV : BaseRequestValues, T> execute(useCase: BaseUseCase<RV, T>, values: RV? = null): Flowable<T> {

        Log.d("UNIDADES","DENTRO DO EXECUTE Do UseCaseHandler: VALUES = " + values.toString() )
        useCase.setRequestValues(values)
        return useCase.run()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
    }

    fun <RV : BaseRequestValues> execute(useCase: BaseCompletableUseCase<RV>, values: RV? = null): Completable {
        Log.d("UNIDADES","DENTRO DO EXECUTE Do UseCaseHandler COMPLETABLE: VALUES = " + values.toString() )
        useCase.setRequestValues(values)
        return useCase.run()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
    }
}