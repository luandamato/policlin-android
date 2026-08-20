package br.com.policlinsaude.ui.auth.welcome

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import br.com.policlinsaude.R
import br.com.policlinsaude.databinding.ActivityWelcomeBinding
import br.com.policlinsaude.ui.base.BaseActivity
import br.com.policlinsaude.ui.auth.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * MVVM Welcome Activity with ViewBinding
 * Shows initial choice between "I am a client" or "I am not a client"
 */
@AndroidEntryPoint
class WelcomeActivity : BaseActivity<ActivityWelcomeBinding>() {

    private val viewModel: WelcomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setupTermsText()
        setupListeners()
        observeViewModel()
    }

    override fun getViewBinding(inflater: LayoutInflater) =
        ActivityWelcomeBinding.inflate(inflater)

    private fun setupTermsText() {
        with(binding) {
            textViewMsgWhenEntering.setText(
                Html.fromHtml(getString(R.string.msg_when_entering_you_allow)),
                TextView.BufferType.SPANNABLE
            )
        }
    }

    private fun setupListeners() {
        with(binding) {
            buttonIamClient.setOnClickListener {
                viewModel.onIAmClientClicked()
            }
            
            buttonIamNotClient.setOnClickListener {
                viewModel.onIAmNotClientClicked()
            }
            
            textViewMsgWhenEntering.setOnClickListener {
                viewModel.onTermsLinkClicked()
                // TODO: Open privacy policy URL
            }
        }
    }

    private fun observeViewModel() {
        viewModel.action.observe(this, Observer { action ->
            when (action) {
                is WelcomeViewModel.WelcomeAction.GoToLogin -> {
                    // Navigate to login screen
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                    viewModel.actionHandled()
                }
                
                is WelcomeViewModel.WelcomeAction.GoToHome -> {
                    // Navigate to home without login (guest user)
                    // TODO: Navigate to MenuActivity
                    showToast("Navigate to home without login")
                    // startActivity(Intent(this, MenuActivity::class.java))
                    // finish()
                    viewModel.actionHandled()
                }
                
                else -> {
                    // Do nothing
                }
            }
        })
    }

    override fun onBackPressed() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun showToast(message: String) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
    }
}
