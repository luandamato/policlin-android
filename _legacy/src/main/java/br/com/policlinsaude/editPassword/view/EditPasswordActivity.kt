package br.com.policlinsaude.editPassword.view

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import br.com.policlinsaude.R
import br.com.policlinsaude.core.base.BaseActivity
import br.com.policlinsaude.core.helper.DialogHelper
import br.com.policlinsaude.databinding.ActivityEditPasswordBinding
import br.com.policlinsaude.domain.AppConstants.REGEX_MIN_AND_MAX_LENGTH_PASSWORD
import br.com.policlinsaude.domain.exception.MessageErrorException
import br.com.policlinsaude.editPassword.presenter.EditPasswordPresenter
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import javax.inject.Inject

class EditPasswordActivity : BaseActivity(), EditPasswordView {

    @Inject
    lateinit var presenter: EditPasswordPresenter

    private lateinit var binding: ActivityEditPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar(binding.toolbar.toolbar)

        setValidations()
        setClickListener()
    }

    override fun showLoading() {
        binding.loadingView.root.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.loadingView.root.visibility = View.GONE
    }

    override fun showSuccessMessage() {
        DialogHelper.showDialog(
            this,
            R.string.title_success,
            R.string.text_edit_password_success,
            R.string.text_ok,
            listenerPositiveButton = {
                presenter.onSuccessDialogDismissed()
            },
            onDismiss = {
                presenter.onSuccessDialogDismissed()
            }
        )
    }

    override fun showErrorDialog(throwable: Throwable) {
        showDialogTryAgain(
            listenerPositiveButton = { checkFields() },
            message = if (throwable is MessageErrorException) {
                throwable.message.orEmpty()
            } else {
                ""
            }
        )
    }

    override fun closeView() {
        finish()
    }

    private fun setValidations() {
        awesomeValidation.addValidation(
            binding.passwordEditText,
            RegexTemplate.NOT_EMPTY,
            getString(R.string.text_field_required)
        )

        awesomeValidation.addValidation(
            binding.passwordConfirmationEditText,
            RegexTemplate.NOT_EMPTY,
            getString(R.string.text_field_required)
        )

        awesomeValidation.addValidation(
            binding.passwordEditText,
            REGEX_MIN_AND_MAX_LENGTH_PASSWORD,
            getString(R.string.text_min_and_max_password)
        )

        awesomeValidation.addValidation(
            binding.passwordConfirmationEditText,
            binding.passwordEditText,
            getString(R.string.text_two_password_invalid)
        )
    }

    private fun setClickListener() {
        binding.sendButton.setOnClickListener {
            checkFields()
        }
    }

    private fun checkFields() {
        if (awesomeValidation.validate()) {
            presenter.onSendClicked(
                binding.passwordEditText.text.toString()
            )
        }
    }
}