package br.com.policlinsaude.editPassword.view

import android.os.Bundle
import android.view.View
import android.widget.TextView
import br.com.domain.AppConstants.REGEX_MIN_AND_MAX_LENGTH_PASSWORD
import br.com.domain.exception.MessageErrorException
import br.com.policlinsaude.R
import br.com.policlinsaude.core.base.BaseActivity
import br.com.policlinsaude.core.helper.DialogHelper
import br.com.policlinsaude.editPassword.presenter.EditPasswordPresenter
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import kotlinx.android.synthetic.main.activity_edit_password.*
import kotlinx.android.synthetic.main.toolbar.view.*
import javax.inject.Inject

class EditPasswordActivity : BaseActivity(), EditPasswordView {

    @Inject
    lateinit var presenter: EditPasswordPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_password)
        setupToolbar()

        setValidations()
        setClickListener()
    }

    override fun showLoading() {
        loadingView.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loadingView.visibility = View.GONE
    }

    override fun showSuccessMessage() {
        DialogHelper.showDialog(this,
                R.string.title_success,
                R.string.text_edit_password_success,
                R.string.text_ok,
                listenerPositiveButton = { presenter.onSuccessDialogDismissed() },
                onDismiss = { presenter.onSuccessDialogDismissed() })
    }

    override fun showErrorDialog(throwable: Throwable) {
        showDialogTryAgain({ checkFields() },
                message = if (throwable is MessageErrorException) throwable.message!! else "")
    }

    override fun closeView() {
        finish()
    }

    private fun setValidations() {
        awesomeValidation.addValidation(passwordEditText,
                RegexTemplate.NOT_EMPTY, getString(R.string.text_field_required))
        awesomeValidation.addValidation(passwordConfirmationEditText,
                RegexTemplate.NOT_EMPTY, getString(R.string.text_field_required))
        awesomeValidation.addValidation(passwordEditText,
                REGEX_MIN_AND_MAX_LENGTH_PASSWORD, getString(R.string.text_min_and_max_password))
        awesomeValidation.addValidation(passwordConfirmationEditText, passwordEditText,
                getString(R.string.text_two_password_invalid))
    }

    private fun setClickListener() {
        sendButton.setOnClickListener {
            checkFields()
        }
    }

    private fun checkFields() {
        if (awesomeValidation.validate()) {
            presenter.onSendClicked(passwordEditText.text.toString())
        }
    }
}