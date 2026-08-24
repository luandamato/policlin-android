package br.com.policlinsaude.forgotPassword.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import br.com.policlinsaude.domain.exception.MessageErrorException
import br.com.policlinsaude.R
import br.com.policlinsaude.core.base.BaseActivity
import br.com.policlinsaude.core.helper.DialogHelper
import br.com.policlinsaude.databinding.ActivityForgotPasswordBinding
import br.com.policlinsaude.forgotPassword.presenter.ForgotPasswordPresenter
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import dagger.android.AndroidInjection
import javax.inject.Inject

class ForgotPasswordActivity : BaseActivity(), ForgotPasswordView {

    companion object {

        fun start(activity: Activity) {
            val intent = Intent(activity, ForgotPasswordActivity::class.java)
            activity.startActivity(intent)
        }
    }

    @Inject
    lateinit var presenter: ForgotPasswordPresenter

    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AndroidInjection.inject(this)
        setOnClickListeners()

        addValidationFields()
    }

    private fun addValidationFields() {
        awesomeValidation.addValidation(binding.editTextRegister,
                RegexTemplate.NOT_EMPTY, getString(R.string.text_field_required))
        awesomeValidation.addValidation(binding.editTextOrder,
                RegexTemplate.NOT_EMPTY, getString(R.string.text_field_required))
        awesomeValidation.addValidation(binding.editTextEmail
                , Patterns.EMAIL_ADDRESS, getString(R.string.text_email_invalid))
        awesomeValidation.addValidation(binding.editTextEmail,
                RegexTemplate.NOT_EMPTY, getString(R.string.text_field_required))
    }

    private fun setOnClickListeners() {
        binding.buttonRecoverPassword.setOnClickListener {
            if (awesomeValidation.validate()) {
                presenter.clickedButtonRecoverPassword()
            }
        }
        binding.buttonBack.setOnClickListener {
            presenter.clickedButtonBack()
        }
    }

    override fun showDialogError(it: Throwable) {
        val listener = {
            presenter.clickedButtonRecoverPassword()
        }
        showDialogTryAgain(listenerPositiveButton = listener,
                message = if (it is MessageErrorException) it.message!! else "")
    }

    override fun showLoading() {
        binding.loadingView.root.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.loadingView.root.visibility = View.GONE
    }

    override fun showButtonRecoverPassword() {
        binding.buttonRecoverPassword.visibility = View.VISIBLE
    }

    override fun hideButtonRecoverPassword() {
        binding.buttonRecoverPassword.visibility = View.GONE
    }

    override fun getEmail(): String = binding.editTextEmail.text.toString()

    override fun getRegister(): String = binding.editTextRegister.text.toString()

    override fun getOrder(): String = binding.editTextOrder.text.toString()

    override fun showEmailSentDialog() {
        DialogHelper.showDialog(context = this,
                title = R.string.title_success,
                message = R.string.text_email_sent,
                messagePositiveButton = R.string.text_ok,
                listenerPositiveButton = { presenter.onEmailSentDialogOkClicked() })
    }
}