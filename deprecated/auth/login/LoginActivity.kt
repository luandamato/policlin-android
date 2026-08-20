package br.com.policlinsaude.ui.auth.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import br.com.policlinsaude.R
import br.com.policlinsaude.databinding.ActivityLoginBinding
import br.com.policlinsaude.ui.base.BaseActivity
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint

/**
 * MVVM Login Activity with ViewBinding
 * Handles user authentication
 */
@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private val viewModel: LoginViewModel by viewModels()
    private var isPasswordVisible = false
    private lateinit var awesomeValidation: AwesomeValidation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        awesomeValidation = AwesomeValidation(com.basgeekball.awesomevalidation.utility.ValidationStyle.UNDERLABEL)
        
        // Initialize Firebase for FCM token
        FirebaseApp.initializeApp(this)
        
        // Check if user already has a valid token
        viewModel.checkValidToken()
        
        // Setup HTML text for terms/privacy
        setupTermsText()
        
        // Setup listeners
        setupListeners()
        
        // Observe ViewModel state
        observeViewModel()
    }

    override fun getViewBinding(inflater: LayoutInflater) =
        ActivityLoginBinding.inflate(inflater)

    private fun setupTermsText() {
        with(binding) {
            textViewMsgWhenEntering.setText(
                Html.fromHtml(getString(R.string.msg_when_entering_you_allow)),
                TextView.BufferType.SPANNABLE
            )
        }
    }

    private fun setupListeners() {
        with(binding) {
            buttonEnter.setOnClickListener {
                if (validateForm()) {
                    getFirebaseTokenAndLogin()
                }
            }
            
            buttonForgotPassword.setOnClickListener {
                // TODO: Navigate to Forgot Password screen
                showToast("Forgot Password not implemented yet")
            }
            
            buttonNotHasPassword.setOnClickListener {
                // TODO: Navigate to Not Has Password screen
                showToast("Not Has Password not implemented yet")
            }
            
            buttonIamNotClient.setOnClickListener {
                // Navigate to Welcome/Home without login
                // TODO: Navigate to home without login
                showToast("Navigate to home without login")
            }
            
            imgEye.setOnClickListener {
                viewModel.togglePasswordVisibility()
            }
            
            textViewMsgWhenEntering.setOnClickListener {
                // TODO: Open privacy policy URL
                showToast("Open privacy policy URL")
            }
        }
    }

    private fun observeViewModel() {
        // Observe login state
        viewModel.loginState.observe(this, Observer { state ->
            when (state) {
                is LoginViewModel.LoginState.Idle -> {
                    binding.apply {
                        editTextRegister.isEnabled = true
                        editTextOrder.isEnabled = true
                        editTextPassword.isEnabled = true
                        buttonEnter.isEnabled = true
                        buttonEnter.visibility = View.VISIBLE
                        login_progressbar.visibility = View.GONE
                        loading_container.visibility = View.GONE
                    }
                    setupValidationFields()
                }
                
                is LoginViewModel.LoginState.Loading -> {
                    binding.apply {
                        editTextRegister.isEnabled = false
                        editTextOrder.isEnabled = false
                        editTextPassword.isEnabled = false
                        buttonEnter.visibility = View.GONE
                        login_progressbar.visibility = View.VISIBLE
                    }
                }
                
                is LoginViewModel.LoginState.CheckingToken -> {
                    binding.loading_container.visibility = View.VISIBLE
                }
                
                is LoginViewModel.LoginState.Success -> {
                    binding.loading_container.visibility = View.GONE
                    // Navigation is handled in navigateToHome observer
                }
                
                is LoginViewModel.LoginState.Error -> {
                    binding.apply {
                        editTextRegister.isEnabled = true
                        editTextOrder.isEnabled = true
                        editTextPassword.isEnabled = true
                        buttonEnter.visibility = View.VISIBLE
                        login_progressbar.visibility = View.GONE
                        loading_container.visibility = View.GONE
                    }
                    showError(state.message)
                }
                
                is LoginViewModel.LoginState.InvalidCredentials -> {
                    showError(getString(R.string.text_field_required))
                }
            }
        })
        
        // Observe password visibility
        viewModel.passwordVisibility.observe(this, Observer { visibility ->
            with(binding) {
                when (visibility) {
                    LoginViewModel.PasswordVisibility.Visible -> {
                        imgEye.setImageResource(R.drawable.ic_action_eye_open)
                        editTextPassword.transformationMethod = null
                        isPasswordVisible = true
                    }
                    LoginViewModel.PasswordVisibility.Hidden -> {
                        imgEye.setImageResource(R.drawable.ic_action_eye_closed)
                        editTextPassword.transformationMethod = PasswordTransformationMethod()
                        isPasswordVisible = false
                    }
                }
                // Keep cursor position
                editTextPassword.setSelection(editTextPassword.text?.length ?: 0)
            }
        })
        
        // Observe navigation to home
        viewModel.navigateToHome.observe(this, Observer { shouldNavigate ->
            if (shouldNavigate) {
                // TODO: Navigate to MenuActivity (home)
                showToast("Navigating to home...")
                viewModel.navigationHandled()
                // startActivity(Intent(this, MenuActivity::class.java))
                // finish()
            }
        })
    }

    private fun setupValidationFields() {
        with(binding) {
            awesomeValidation.clear()
            awesomeValidation.addValidation(
                editTextRegister,
                RegexTemplate.NOT_EMPTY,
                getString(R.string.text_field_required)
            )
            awesomeValidation.addValidation(
                editTextPassword,
                RegexTemplate.NOT_EMPTY,
                getString(R.string.text_field_required)
            )
            awesomeValidation.addValidation(
                editTextOrder,
                RegexTemplate.NOT_EMPTY,
                getString(R.string.text_field_required)
            )
        }
    }

    private fun validateForm(): Boolean {
        return awesomeValidation.validate()
    }

    private fun getFirebaseTokenAndLogin() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("LoginActivity", "Fetching FCM token failed", task.exception)
                // Use empty token if Firebase fails
                performLogin("")
                return@addOnCompleteListener
            }
            
            val token = task.result
            performLogin(token)
        }
    }

    private fun performLogin(firebaseToken: String) {
        with(binding) {
            val register = editTextRegister.text.toString()
            val order = editTextOrder.text.toString()
            val password = editTextPassword.text.toString()
            
            viewModel.login(register, order, password, firebaseToken)
        }
    }

    override fun onBackPressed() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun showToast(message: String) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
    }
}
