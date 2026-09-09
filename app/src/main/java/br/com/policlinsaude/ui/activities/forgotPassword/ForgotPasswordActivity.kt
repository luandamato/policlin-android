package br.com.policlinsaude.ui.activities.forgotPassword

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import br.com.policlinsaude.R
import br.com.policlinsaude.databinding.ActivityForgotPasswordBinding
import br.com.policlinsaude.ui.dialogs.DialogHelper
import org.koin.androidx.viewmodel.ext.android.viewModel

class ForgotPasswordActivity : AppCompatActivity() {

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, ForgotPasswordActivity::class.java)
            activity.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityForgotPasswordBinding
    private val viewModel: ForgotPasswordViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeViewModel()
        setupListeners()
    }

    private fun observeViewModel() {
        viewModel.loading.observe(this) { loading ->
            binding.loadingView.isVisible = loading
            binding.buttonRecoverPassword.isEnabled = !loading
        }

        viewModel.event.observe(this) { event ->
            when (event) {
                ForgotPasswordEvent.ShowSuccess -> showEmailSentDialog()
                is ForgotPasswordEvent.ShowError -> showErrorDialog(event.message)
                ForgotPasswordEvent.Finish -> finish()
            }
        }
    }

    private fun setupListeners() {
        binding.buttonRecoverPassword.setOnClickListener {
            if (validateFields()) {
                viewModel.recoverPassword(
                    register = binding.editTextRegister.text?.toString().orEmpty(),
                    order = binding.editTextOrder.text?.toString().orEmpty(),
                    email = binding.editTextEmail.text?.toString().orEmpty()
                )
            }
        }

        binding.buttonBack.setOnClickListener {
            viewModel.onBackClicked()
        }
    }

    private fun validateFields(): Boolean {
        var valid = true
        binding.inputLayoutRegister.error = null
        binding.inputLayoutOrder.error = null
        binding.inputLayoutEmail.error = null

        val register = binding.editTextRegister.text?.toString().orEmpty()
        val order = binding.editTextOrder.text?.toString().orEmpty()
        val email = binding.editTextEmail.text?.toString().orEmpty()

        if (register.isBlank()) {
            binding.inputLayoutRegister.error = getString(R.string.text_field_required)
            valid = false
        }

        if (order.isBlank()) {
            binding.inputLayoutOrder.error = getString(R.string.text_field_required)
            valid = false
        }

        if (email.isBlank()) {
            binding.inputLayoutEmail.error = getString(R.string.text_field_required)
            valid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.inputLayoutEmail.error = getString(R.string.text_email_invalid)
            valid = false
        }

        return valid
    }

    private fun showErrorDialog(message: String) {
        DialogHelper.showErrorDialog(this, message)
    }

    private fun showEmailSentDialog() {
        DialogHelper.showDialog(
            context = this,
            title = getString(R.string.title_success),
            message = getString(R.string.text_email_sent),
            messagePositiveButton = getString(R.string.text_ok),
            listenerPositiveButton = { viewModel.onSuccessDialogOkClicked() }
        )
    }
}
