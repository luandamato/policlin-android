package br.com.policlinsaude.ui.activities.notHasPassword

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import br.com.policlinsaude.R
import br.com.policlinsaude.databinding.ActivityNotHasPasswordBinding
import br.com.policlinsaude.ui.dialogs.DialogHelper
import br.com.policlinsaude.ui.fragments.notHasPassword.CreatePasswordFragment
import br.com.policlinsaude.ui.fragments.notHasPassword.NotHasPasswordFragmentPagerAdapter
import br.com.policlinsaude.ui.fragments.notHasPassword.PersonalDataFragment
import br.com.policlinsaude.ui.fragments.notHasPassword.PlanDataFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Fluxo de cadastro de senha para usuários que ainda não possuem senha
 * (bate com o legado `notHasPassword`).
 *
 * Stepper em 3 passos (Dados Pessoais → Dados do Plano → Criar Senha),
 * MVVM: UI → NotHasPasswordViewModel → AppRepository (checkPlan / registerPassword).
 */
class NotHasPasswordActivity : AppCompatActivity() {

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, NotHasPasswordActivity::class.java)
            activity.startActivity(intent)
        }
    }

    val viewModel: NotHasPasswordViewModel by viewModel()

    private lateinit var binding: ActivityNotHasPasswordBinding
    private lateinit var adapter: NotHasPasswordFragmentPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotHasPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = NotHasPasswordFragmentPagerAdapter(this)
        binding.viewPager.adapter = adapter
        binding.viewPager.isUserInputEnabled = false // desabilita swipe

        observeViewModel()
        setupNavigation()
    }

    // =====================================================================
    // Observers (estado/eventos do ViewModel)
    // =====================================================================
    private fun observeViewModel() {
        viewModel.loading.observe(this) { loading ->
            showLoading(loading)
        }

        viewModel.event.observe(this) { event ->
            when (event) {
                is NotHasPasswordEvent.ShowError -> showDialogError(event.message)
                is NotHasPasswordEvent.ShowSuccess -> showSuccessDialog(event.message)
                NotHasPasswordEvent.Finish -> finish()
            }
        }
    }

    // =====================================================================
    // Navegação do stepper
    // =====================================================================
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
        val currentFragment =
            supportFragmentManager.findFragmentByTag("f" + binding.viewPager.currentItem)

        when (currentFragment) {
            is PersonalDataFragment -> {
                if (currentFragment.validateAndSave()) goToNextStep()
            }
            is PlanDataFragment -> {
                currentFragment.validateAndSave { success ->
                    if (success) goToNextStep()
                }
            }
            is CreatePasswordFragment -> {
                if (currentFragment.validateAndSave()) viewModel.clickedButtonComplete()
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
        binding.btnNext.text =
            if (isLastStep) getString(R.string.action_create) else getString(R.string.action_next)
        binding.btnBack.text = getString(R.string.action_back)
    }

    // =====================================================================
    // Helpers visuais (loading, dialogs, toasts)
    // =====================================================================
    private fun showLoading(loading: Boolean) {
        binding.viewPager.isVisible = !loading
        binding.navigationButtons.isVisible = !loading
        binding.progressBarHolder.isVisible = loading
    }

    private fun showDialogError(message: String) {
        DialogHelper.showDialogTryAgain(
            context = this,
            message = message,
            listenerPositiveButton = { handleNextStep() }
        )
    }

    private fun showSuccessDialog(message: String) {
        DialogHelper.showDialog(
            context = this,
            title = getString(R.string.title_success),
            message = message,
            messagePositiveButton = getString(R.string.text_ok),
            listenerPositiveButton = { viewModel.onSuccessDialogDismissed() },
            onDismiss = { viewModel.onSuccessDialogDismissed() }
        )
    }

    fun showPhotoNeeded() {
        DialogHelper.showDialog(
            this,
            R.string.title_error_oops,
            R.string.text_photo_needed,
            R.string.text_ok
        )
    }

    fun showToastMessage(@StringRes message: Int) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}