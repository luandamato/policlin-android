package br.com.policlinsaude.ui.activities.token

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import br.com.policlinsaude.R
import br.com.policlinsaude.databinding.ActivityTokenBinding
import br.com.policlinsaude.ui.fragments.token.TokenBeneficiariosFragment
import br.com.policlinsaude.ui.fragments.token.TokenFragment
import br.com.policlinsaude.ui.views.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class TokenActivity : BaseActivity() {

    companion object {

        fun start(activity: Activity) {
            val intent = Intent(activity, TokenActivity::class.java)
            activity.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityTokenBinding

    val viewModel: TokenViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTokenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.tokenToolbar.toolbar)
        supportActionBar?.title = getString(R.string.title_research_co_participation)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, TokenFragment.newInstance())
                .commit()
        }
    }

    fun showBackButton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.tokenToolbar.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    fun showList() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, TokenBeneficiariosFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }
}