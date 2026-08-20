package br.com.policlinsaude.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import br.com.policlinsaude.data.services.ServiceLocator
import br.com.policlinsaude.data.services.ApiService
import android.content.SharedPreferences

/**
 * Base Activity com ViewBinding genérico
 * Fornece acesso a Services e SharedPreferences
 */
abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    protected lateinit var binding: VB

    protected val apiService: ApiService get() = ServiceLocator.apiService()
    protected val preferences: SharedPreferences get() = ServiceLocator.preferences()

    abstract fun createBinding(): VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = createBinding()
        setContentView(binding.root)
    }

    protected fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, duration).show()
    }

    protected fun showError(error: Throwable) {
        showToast(error.message ?: "Erro desconhecido")
    }

    protected fun getToken(): String? {
        return preferences.getString("auth_token", null)
    }

    protected fun hasToken(): Boolean {
        return !getToken().isNullOrEmpty()
    }

    protected fun saveToken(token: String) {
        preferences.edit().putString("auth_token", token).apply()
    }

    protected fun clearToken() {
        preferences.edit().remove("auth_token").apply()
    }
}
