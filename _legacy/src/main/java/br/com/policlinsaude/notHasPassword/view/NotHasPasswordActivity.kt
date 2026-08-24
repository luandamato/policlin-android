package br.com.policlinsaude.notHasPassword.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import br.com.policlinsaude.domain.exception.MessageErrorException
import br.com.policlinsaude.R
import br.com.policlinsaude.core.base.BaseActivity
import br.com.policlinsaude.core.helper.DialogHelper
import br.com.policlinsaude.databinding.ActivityNotHasPasswordBinding
import br.com.policlinsaude.notHasPassword.presenter.NotHasPasswordPresenter
import br.com.policlinsaude.notHasPassword.view.adapter.NotHasPasswordFragmentPagerAdapter
import br.com.policlinsaude.notHasPassword.view.adapter.NotHasPasswordStepperEnum
import br.com.policlinsaude.notHasPassword.view.steppers.CreatePasswordFragment
import br.com.policlinsaude.notHasPassword.view.steppers.PersonalDataFragment
import br.com.policlinsaude.notHasPassword.view.steppers.PlanDataFragment
import javax.inject.Inject

class NotHasPasswordActivity : BaseActivity(), NotHasPasswordView {
    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, NotHasPasswordActivity::class.java)
            activity.startActivity(intent)
        }
    }

    @Inject
    lateinit var presenter: NotHasPasswordPresenter

    private lateinit var adapter: NotHasPasswordFragmentPagerAdapter

    private lateinit var binding: ActivityNotHasPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNotHasPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = NotHasPasswordFragmentPagerAdapter(this)
        binding.viewPager.adapter = adapter
        binding.viewPager.isUserInputEnabled = false // Disable swiping

        setupNavigation()
    }

    private fun setupNavigation() {
        binding.btnBack.setOnClickListener {
            if (binding.viewPager.currentItem > 0) {
                binding.viewPager.currentItem -= 1
                updateButtons()
            } else {
                finish()
            }
        }

        binding.btnNext.setOnClickListener {
            handleNextStep()
        }

        updateButtons()
    }

    private fun handleNextStep() {
        val currentFragment = supportFragmentManager.findFragmentByTag("f" + binding.viewPager.currentItem)
        
        when (currentFragment) {
            is PersonalDataFragment -> {
                if (currentFragment.validateAndSave()) {
                    goToNextStep()
                }
            }
            is PlanDataFragment -> {
                currentFragment.validateAndSave { success ->
                    if (success) goToNextStep()
                }
            }
            is CreatePasswordFragment -> {
                if (currentFragment.validateAndSave()) {
                    presenter.clickedButtonComplete()
                }
            }
        }
    }

    private fun goToNextStep() {
        if (binding.viewPager.currentItem < adapter.itemCount - 1) {
            binding.viewPager.currentItem += 1
            updateButtons()
        }
    }

    private fun updateButtons() {
        val isLastStep = binding.viewPager.currentItem == adapter.itemCount - 1
        binding.btnNext.text = if (isLastStep) getString(R.string.action_create) else getString(R.string.action_next)
        binding.btnBack.text = if (binding.viewPager.currentItem == 0) getString(R.string.action_back) else getString(R.string.action_back)
    }

    override fun showLoading() {
        binding.viewPager.visibility = View.GONE
        binding.navigationButtons.visibility = View.GONE
        binding.progressBarHolder.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.viewPager.visibility = View.VISIBLE
        binding.navigationButtons.visibility = View.VISIBLE
        binding.progressBarHolder.visibility = View.GONE
    }

    override fun showDialogError(it: Throwable) {
        val listener = {
            handleNextStep()
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