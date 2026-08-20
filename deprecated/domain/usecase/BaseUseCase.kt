package br.com.policlinsaude.domain.usecase

import io.reactivex.Flowable

/**
 * Classe base abstrata para todos os UseCases
 * Define um padrão padrão para execução de casos de uso
 */
abstract class BaseUseCase<in Params, out Result> {

    abstract fun execute(params: Params? = null): Flowable<Result>

    open class None
}
