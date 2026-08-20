package br.com.policlinsaude.ui.activities.login

import android.os.Bundle
import android.os.Build
import android.text.Html
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import br.com.policlinsaude.R
import br.com.policlinsaude.databinding.ActivityLoginBinding
import br.com.policlinsaude.ui.activities.BaseActivity
import br.com.policlinsaude.util.extensions.showToast
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.utility.ValidationStyle
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch
import br.com.policlinsaude.data.models.LoginRequest

/**
 * LoginActivity simplificada - sem Hilt, apenas ServiceLocator
 */
class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var awesomeValidation: AwesomeValidation
    private var isPasswordVisible = false

    override fun createBinding() = ActivityLoginBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar ViewModel manualmente
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        // Inicializar Firebase
        FirebaseApp.initializeApp(this)

        // Validação
        awesomeValidation = AwesomeValidation(ValidationStyle.UNDERLABEL)
        setupValidation()

        // Setup UI
        setupUI()

        // Observers
        observeViewModel()

        // Check if already logged in
        if (hasToken()) {
            // TODO: Navigate to Home
            showToast("Já tem token salvo")
        }
    }

    private fun setupValidation() {
        awesomeValidation.addValidation(
            binding.editTextRegister,
            RegexTemplate.NOT_EMPTY,
            getString(R.string.text_field_required)
        )
        awesomeValidation.addValidation(
            binding.editTextOrder,
            RegexTemplate.NOT_EMPTY,
            getString(R.string.text_field_required)
        )
        awesomeValidation.addValidation(
            binding.editTextPassword,
            RegexTemplate.NOT_EMPTY,
            getString(R.string.text_field_required)
        )
    }

    private fun setupUI() {
        binding.apply {
            buttonEnter.setOnClickListener {
                if (awesomeValidation.validate()) {
                    getFirebaseTokenAndLogin()
                }
            }

            imgEye.setOnClickListener {
                togglePasswordVisibility()
            }

            buttonForgotPassword.setOnClickListener {
                showToast("Forgot password not implemented")
            }

            buttonNotHasPassword.setOnClickListener {
                showToast("Not has password not implemented")
            }

            buttonIamNotClient.setOnClickListener {
                // Navigate to home without login
                showToast("Navigate to home without login")
            }

            textViewMsgWhenEntering.text = Html.fromHtml(
                getString(R.string.msg_when_entering_you_allow),
                Html.FROM_HTML_MODE_COMPACT
            )

            textViewMsgWhenEntering.setOnClickListener {
                // Open privacy URL
                showToast("Open privacy policy")
            }
        }
    }

    private fun togglePasswordVisibility() {
        binding.apply {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                imgEye.setImageResource(R.drawable.ic_action_eye_open)
                editTextPassword.transformationMethod = null
            } else {
                imgEye.setImageResource(R.drawable.ic_action_eye_closed)
                editTextPassword.transformationMethod = PasswordTransformationMethod()
            }
            editTextPassword.setSelection(editTextPassword.text?.length ?: 0)
        }
    }

    private fun getFirebaseTokenAndLogin() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                performLogin("")
                return@addOnCompleteListener
            }
            val token = task.result
            performLogin(token)
        }
    }

    private fun performLogin(firebaseToken: String) {
        binding.apply {
            val register = editTextRegister.text.toString()
            val order = editTextOrder.text.toString()
            val password = editTextPassword.text.toString()

            val request = LoginRequest(
                register = register,
                order = order,
                password = password,
                firebaseToken = firebaseToken,
                osVersion = Build.VERSION.RELEASE
            )

            viewModel.login(request)
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.loginStatus.collect { status ->
                when (status) {
                    is LoginStatus.Loading -> {
                        binding.login_progressbar.show()
                        binding.buttonEnter.isEnabled = false
                    }
                    is LoginStatus.Success -> {
                        binding.login_progressbar.hide()
                        showToast("Login realizado com sucesso!")
                        // TODO: Navigate to Home
                    }
                    is LoginStatus.Error -> {
                        binding.login_progressbar.hide()
                        binding.buttonEnter.isEnabled = true
                        showToast(status.message)
                    }
                    else -> {
                        binding.login_progressbar.hide()
                        binding.buttonEnter.isEnabled = true
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        val intent = android.content.Intent(android.content.Intent.ACTION_MAIN)
        intent.addCategory(android.content.Intent.CATEGORY_HOME)
        intent.flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}

// Status sealed class
sealed class LoginStatus {
    object Idle : LoginStatus()
    object Loading : LoginStatus()
    object Success : LoginStatus()
    data class Error(val message: String) : LoginStatus()
}
