package br.com.policlinsaude.ui.activities.editPhone

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import br.com.policlinsaude.R
import br.com.policlinsaude.ui.views.BaseActivity
import br.com.policlinsaude.ui.dialogs.DialogHelper
import br.com.policlinsaude.databinding.ActivityEditPhoneBinding
import br.com.policlinsaude.util.helpers.Mask
import com.redmadrobot.inputmask.MaskedTextChangedListener
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Activity para alteração de telefone (MVVM).
 */
class EditPhoneActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, EditPhoneActivity::class.java))
        }
    }

    private val viewModel: EditPhoneViewModel by viewModel()
    private lateinit var binding: ActivityEditPhoneBinding
    private var extractedPhone: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPhoneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar(binding.toolbar.toolbar)
        setupViews()
        setupObservers()
    }

    private fun setupViews() {
        // Aplica a máscara e captura o valor extraído (apenas números)
        Mask.applyToView(
            Mask.PHONE,
            binding.phoneTextView,
            object : MaskedTextChangedListener.ValueListener {
                override fun onTextChanged(
                    maskFilled: Boolean,
                    extractedValue: String,
                    formattedValue: String,
                    tailPlaceholder: String
                ) {
                    extractedPhone = extractedValue
                }
            }
        )

        binding.sendButton.setOnClickListener {
            viewModel.onSendClicked(extractedPhone)
        }
    }

    private fun setupObservers() {
        viewModel.loading.observe(this) { isLoading ->
            binding.loadingView.root.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.event.observe(this) { event ->
            when (event) {
                is EditPhoneEvent.ShowSuccess -> showSuccessMessage()
                is EditPhoneEvent.ShowError -> showError(message = event.message)
            }
        }
    }

    private fun showSuccessMessage() {
        DialogHelper.showDialog(
            this,
            R.string.title_success,
            R.string.text_edit_phone_success,
            R.string.text_ok,
            listenerPositiveButton = { finish() },
            onDismiss = { finish() }
        )
    }
}
