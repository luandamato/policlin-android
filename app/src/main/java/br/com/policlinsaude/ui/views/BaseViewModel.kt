package br.com.policlinsaude.ui.views

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

/**
 * ViewModel base da nova arquitetura.
 *
 * Expõe helpers de coroutines com tratamento de erro central,
 * para serem usados pelas features.
 */
abstract class BaseViewModel : ViewModel() {

    protected val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError(throwable)
    }

    /** Executa uma coroutine no escopo do ViewModel tratando erros. */
    protected fun launch(block: suspend kotlinx.coroutines.CoroutineScope.() -> Unit) {
        viewModelScope.launch(exceptionHandler) {
            block()
        }
    }

    /** Callback de erro padrão; sobrescreva por feature. */
    protected open fun onError(throwable: Throwable) {
        // no-op por padrão
    }
}