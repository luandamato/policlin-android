package br.com.policlinsaude.ui.activities.editPassword

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import br.com.policlinsaude.R
import br.com.policlinsaude.ui.dialogs.DialogHelper
import br.com.policlinsaude.databinding.ActivityEditPasswordBinding
import br.com.policlinsaude.ui.views.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Activity para alteração de senha (MVVM).
 */
class EditPasswordActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, EditPasswordActivity::class.java))
        }
    }

    private val viewModel: EditPasswordViewModel by viewModel()
    private lateinit var binding: ActivityEditPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar(binding.toolbar.toolbar)
        setupViews()
        setupObservers()
    }

    private fun setupViews() {
        binding.sendButton.setOnClickListener {
            val password = binding.passwordEditText.text.toString()
            val confirmation = binding.passwordConfirmationEditText.text.toString()

            if (password.isBlank() || confirmation.isBlank()) {
                showError(message = getString(R.string.text_field_required))
                return@setOnClickListener
            }

            if (password != confirmation) {
                showError(message = getString(R.string.text_two_password_invalid))
                return@setOnClickListener
            }

            if (password.length < 6) { // Mantendo coerência com validações padrão
                showError(message = getString(R.string.text_min_and_max_password))
                return@setOnClickListener
            }

            viewModel.onSendClicked(password)
        }
    }

    private fun setupObservers() {
        viewModel.loading.observe(this) { isLoading ->
            binding.loadingView.root.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.event.observe(this) { event ->
            when (event) {
                is EditPasswordEvent.ShowSuccess -> showSuccessMessage()
                is EditPasswordEvent.ShowError -> showError(message = event.message)
            }
        }
    }

    private fun showSuccessMessage() {
        DialogHelper.showDialog(
            this,
            R.string.title_success,
            R.string.text_edit_password_success,
            R.string.text_ok,
            listenerPositiveButton = { finish() },
            onDismiss = { finish() }
        )
    }
}
