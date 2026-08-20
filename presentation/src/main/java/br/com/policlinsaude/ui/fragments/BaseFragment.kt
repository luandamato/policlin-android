package br.com.policlinsaude.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import br.com.policlinsaude.data.services.ServiceLocator
import br.com.policlinsaude.data.services.ApiService
import android.content.SharedPreferences

/**
 * Base Fragment com ViewBinding genérico
 * Fornece acesso a Services e SharedPreferences
 */
abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    protected var _binding: VB? = null
    protected val binding get() = _binding!!

    protected val apiService: ApiService get() = ServiceLocator.apiService()
    protected val preferences: SharedPreferences get() = ServiceLocator.preferences()

    abstract fun createBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = createBinding(inflater, container)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, message, duration).show()
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
}
