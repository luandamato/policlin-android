package br.com.policlinsaude.ui.activities

import androidx.lifecycle.ViewModel
import br.com.policlinsaude.data.services.ServiceLocator

/**
 * Base ViewModel
 * Fornece acesso a Services compartilhados
 */
abstract class BaseViewModel : ViewModel() {

    protected val apiService get() = ServiceLocator.apiService()
    protected val preferences get() = ServiceLocator.preferences()

    protected fun getToken(): String? {
        return preferences.getString("auth_token", null)
    }

    protected fun saveToken(token: String) {
        preferences.edit().putString("auth_token", token).apply()
    }
}
