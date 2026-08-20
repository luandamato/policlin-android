package br.com.policlinsaude.ui.legacy.notHasPassword.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import br.com.domain.exception.MessageErrorException
import br.com.policlinsaude.R
import br.com.policlinsaude.core.base.BaseActivity
import br.com.policlinsaude.core.helper.DialogHelper
import br.com.policlinsaude.notHasPassword.presenter.NotHasPasswordPresenter
import br.com.policlinsaude.notHasPassword.view.adapter.NotHasPasswordFragmentPagerAdapter
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.activity_not_has_password.*
import javax.inject.Inject

class NotHasPasswordActivity : BaseActivity(), NotHasPasswordView,
        StepperLayout.StepperListener {
    companion object {

        fun start(activity: Activity) {
            val intent = Intent(activity, NotHasPasswordActivity::class.java)
            activity.startActivity(intent)
        }
    }

    @Inject
    lateinit var presenter: NotHasPasswordPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_not_has_password)

        stepperLayout.adapter = NotHasPasswordFragmentPagerAdapter(supportFragmentManager,
                this, presenter)

        stepperLayout.setListener(this)
    }


    override fun onStepSelected(newStepPosition: Int) {}

    override fun onError(verificationError: VerificationError?) {}

    override fun onReturn() {}

    override fun onCompleted(completeButton: View?) {
        presenter.clickedButtonComplete()
    }

    override fun showLoading() {
        stepperLayout.visibility = View.GONE
        progressBarHolder.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        stepperLayout.visibility = View.VISIBLE
        progressBarHolder.visibility = View.GONE
    }

    override fun showDialogError(it: Throwable) {
        val listener = {
            onCompleted(null)
        }
        showDialogTryAgain(listenerPositiveButton = listener,
                message = if (it is MessageErrorException) it.message!! else "")
    }

    override fun showImagePickError() {
        showToast(R.string.text_image_pick_error)
    }

    override fun showPhotoNeededError() {
        DialogHelper.showDialog(this,
                R.string.title_error_oops,
                R.string.text_photo_needed,
                R.string.text_ok)
    }

    override fun showSuccessDialog(message: String) {
        DialogHelper.showDialog(this,
                getString(R.string.title_success),
                message,
                getString(R.string.text_ok),
                null,
                { presenter.onSuccessDialogDismissed() },
                null,
                { presenter.onSuccessDialogDismissed() })
    }
}