package br.com.policlinsaude.util.helpers

/**
 * Estado padrão de tela para as features MVVM (StateFlow).
 *
 * Uso:
 * ```
 * private val _state = MutableStateFlow<UiState<T>>(UiState.Idle)
 * val state: StateFlow<UiState<T>> = _state.asStateFlow()
 * ```
 */
sealed interface UiState<out T> {

    data object Idle : UiState<Nothing>
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String, val throwable: Throwable? = null) : UiState<Nothing>
    data object Empty : UiState<Nothing>
}