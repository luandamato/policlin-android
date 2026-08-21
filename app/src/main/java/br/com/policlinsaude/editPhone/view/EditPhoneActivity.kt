package br.com.policlinsaude.editPhone.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import br.com.policlinsaude.domain.exception.MessageErrorException
import br.com.policlinsaude.R
import br.com.policlinsaude.core.base.BaseActivity
import br.com.policlinsaude.core.helper.DialogHelper
import br.com.policlinsaude.core.helper.InvalidData
import br.com.policlinsaude.core.helper.MaskUtils
import br.com.policlinsaude.editPhone.presenter.EditPhonePresenter
import com.redmadrobot.inputmask.MaskedTextChangedListener
import kotlinx.android.synthetic.main.activity_edit_phone.*
import javax.inject.Inject

class EditPhoneActivity : BaseActivity(), EditPhoneView {

    companion object {
        private const val REGEX_MIN_PHONE = "^[\\s\\S]{15,}\$"

        fun start(context: Context) {
            context.startActivity(Intent(context, EditPhoneActivity::class.java))
        }
    }

    @Inject
    lateinit var presenter: EditPhonePresenter

    private var phone: String = InvalidData.UNINITIALIZED.getString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_phone)
        setupToolbar()

        setValidations()
        setClickListener()
        setMasks()
    }

    private fun setMasks() {
        MaskUtils.applyMaskToView(MaskUtils.PHONE, phoneTextView, object : MaskedTextChangedListener.ValueListener {
            override fun onTextChanged(maskFilled: Boolean, extractedValue: String) {
                phone = extractedValue
            }
        })
    }

    override fun closeView() {
        finish()
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
                R.string.text_edit_phone_success,
                R.string.text_ok,
                listenerPositiveButton = { presenter.onSuccessDialogDismissed() },
                onDismiss = { presenter.onSuccessDialogDismissed() })
    }

    override fun showErrorDialog(throwable: Throwable) {
        showDialogTryAgain({ checkFields() },
                message = if (throwable is MessageErrorException) throwable.message!! else "")
    }

    private fun setValidations() {
        awesomeValidation.addValidation(phoneTextView,
                REGEX_MIN_PHONE, getString(R.string.text_phone_invalid))
    }

    private fun setClickListener() {
        sendButton.setOnClickListener {
            checkFields()
        }
    }

    private fun checkFields() {
        if (awesomeValidation.validate()) {
            presenter.onSendClicked(phone)
        }
    }
}