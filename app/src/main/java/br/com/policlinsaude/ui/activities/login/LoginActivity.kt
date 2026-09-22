package br.com.policlinsaude.ui.activities.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import br.com.policlinsaude.R
import br.com.policlinsaude.databinding.ActivityLoginBinding
import br.com.policlinsaude.ui.activities.forgotPassword.ForgotPasswordActivity
import br.com.policlinsaude.ui.activities.home.MenuActivity
import br.com.policlinsaude.ui.activities.notHasPassword.NotHasPasswordActivity
import br.com.policlinsaude.ui.dialogs.DialogHelper
import br.com.policlinsaude.util.extensions.openBrowser
import br.com.policlinsaude.utils.LogManager
import com.google.firebase.messaging.FirebaseMessaging
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Tela de login (MVVM).
 *
 * Fluxo novo (sem Presenter/Navigator):
 * UI → LoginViewModel → AppRepository.onLogin → AppService
 * UI ←── LiveData/Event ── LoginViewModel
 */
class LoginActivity : AppCompatActivity() {

    companion object {
        fun start(activity: android.app.Activity) {
            val intent = Intent(activity, LoginActivity::class.java)
            intent.flags = android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP or android.content.Intent.FLAG_ACTIVITY_NEW_TASK
            activity.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityLoginBinding

    private val viewModel: LoginViewModel by viewModel()

    private var hided: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeViewModel()
        viewModel.checkHasToken()

        binding.textViewMsgWhenEntering.setText(R.string.msg_when_entering_you_allow)
        setupListeners()
    }

    override fun onBackPressed() {
        val a = Intent(Intent.ACTION_MAIN)
        a.addCategory(Intent.CATEGORY_HOME)
        a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(a)
    }

    // =====================================================================
    // Observers (estado/eventos do ViewModel)
    // =====================================================================
    private fun observeViewModel() {
        viewModel.loading.observe(this) { loading ->
            showLoading(loading)
        }

        viewModel.event.observe(this) { event ->
            when (event) {
                is LoginEvent.NavigateToHome -> navigateToHome()
                is LoginEvent.NavigateToHomeWithoutLogin -> navigateToHomeWithoutLogin()
                is LoginEvent.NavigateToForgotPassword -> navigateToForgotPassword()
                is LoginEvent.NavigateToNotHasPassword -> navigateToNotHasPassword()
                is LoginEvent.ShowError -> showDialogError(event.message)
                is LoginEvent.ShowUpdateApp -> showDialogUpdateApp(event.message)
            }
        }
    }

    private fun setupListeners() {
        binding.buttonEnter.setOnClickListener {
            getFirebaseToken { firebaseToken ->
                if (validateFields()) {
                    viewModel.login(
                        register = binding.editTextRegister.text.toString(),
                        order = binding.editTextOrder.text.toString(),
                        password = binding.editTextPassword.text.toString(),
                        firebaseToken = firebaseToken
                    )
                }
            }
        }

        binding.buttonForgotPassword.setOnClickListener {
            navigateToForgotPassword()
        }

        binding.buttonNotHasPassword.setOnClickListener {
            navigateToNotHasPassword()
        }

        binding.buttonIamNotClient.setOnClickListener {
            navigateToHomeWithoutLogin()
        }

        binding.imgEye.setOnClickListener {
            changeEye()
        }

        binding.textViewMsgWhenEntering.setOnClickListener {
            openBrowser(getString(R.string.url_privacy))
        }
    }

    // =====================================================================
    // Validações
    // =====================================================================
    private fun validateFields(): Boolean {
        var valid = true
        binding.editTextRegister.error = null
        binding.editTextOrder.error = null
        binding.editTextPassword.error = null

        if (binding.editTextRegister.text.isNullOrBlank()) {
            binding.editTextRegister.error = getString(R.string.text_field_required)
            valid = false
        }
        if (binding.editTextOrder.text.isNullOrBlank()) {
            binding.editTextOrder.error = getString(R.string.text_field_required)
            valid = false
        }
        if (binding.editTextPassword.text.isNullOrBlank()) {
            binding.editTextPassword.error = getString(R.string.text_field_required)
            valid = false
        }
        return valid
    }

    // =====================================================================
    // FCM token
    // =====================================================================
    @SuppressLint("MissingPermission")
    private fun getFirebaseToken(onToken: (String) -> Unit) {
        LogManager.d("LoginActivity", "getFirebaseToken() chamado")
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                LogManager.d("LoginActivity", "Falha ao obter token FCM", task.exception)
                return@addOnCompleteListener
            }
            val token = task.result
            LogManager.d("LoginActivity", "token FCM obtido: $token")
            token?.let(onToken)
        }
    }

    // =====================================================================
    // Helper visual
    // =====================================================================
    private fun showLoading(loading: Boolean) {
        binding.loadingContainer.isVisible = loading
        binding.loginProgressbar.isVisible = loading
        binding.buttonEnter.isEnabled = !loading
    }

    private fun changeEye() {
        hided = !hided
        binding.editTextPassword.transformationMethod =
            if (hided) PasswordTransformationMethod() else null
    }

    // =====================================================================
    // Navegação (na UI)
    // =====================================================================
    private fun navigateToHome() {
        MenuActivity.start(this)
        finish()
    }

    private fun navigateToHomeWithoutLogin() {
        MenuActivity.start(this)
    }

    private fun navigateToForgotPassword() {
        ForgotPasswordActivity.start(this)
    }

    private fun navigateToNotHasPassword() {
        NotHasPasswordActivity.start(this)
    }

    private fun showDialogError(message: String) {
        if (message.contains("450") || message.contains("455")) {
            DialogHelper.showUpdateDialog(this, message)
        } else {
            DialogHelper.showErrorDialog(this, message)
        }
    }

    private fun showDialogUpdateApp(message: String) {
        DialogHelper.showUpdateDialog(this, message)
    }
}