package br.com.policlinsaude.ui.activities.incometax

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import br.com.policlinsaude.R
import br.com.policlinsaude.data.models.IncomeTaxItemModel
import br.com.policlinsaude.databinding.ActivityIncomeTaxBinding
import br.com.policlinsaude.ui.dialogs.DialogHelper
import br.com.policlinsaude.util.extensions.openBrowser
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Tela de Informe de Imposto de Renda (MVVM).
 *
 * Migrado do legado `IncomeTaxActivity` (shell) + `IncomeTaxFragment`: agora a
 * tela inteira vive na Activity (sem fragment), com todo o layout diretamente
 * no `activity_income_tax.xml` e sem Navigation Component (mesmo padrão de
 * telas únicas do novo app).
 *
 * Fluxo novo: UI → IncomeTaxViewModel → AppRepository (apiIR).
 */
class IncomeTaxActivity : AppCompatActivity() {

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, IncomeTaxActivity::class.java)
            activity.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityIncomeTaxBinding

    private val viewModel: IncomeTaxViewModel by viewModel()

    private val adapter by lazy { IncomeTaxItemAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIncomeTaxBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        setupObservables()

        viewModel.onGetIncomeTax()
    }

    private fun setupViews() {
        with(binding) {
            recyclerViewIncomeTax.adapter = adapter

            setSupportActionBar(incomeTaxToolbar.toolbar)
            supportActionBar?.title = getString(R.string.title_income_tax)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            incomeTaxToolbar.toolbar.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun setupObservables() {
        viewModel.loading.observe(this) { loading ->
            if (loading) showLoading() else hideLoading()
        }

        viewModel.incomeTax.observe(this) { list ->
            list?.let { incomesTax ->
                if (incomesTax.isEmpty()) {
                    adapter.update(arrayListOf())
                    binding.textviewMsgNone.visibility = View.VISIBLE
                } else {
                    success(incomesTax)
                    binding.textviewMsgNone.visibility = View.GONE
                }
            }
        }

        viewModel.event.observe(this) { event ->
            when (event) {
                IncomeTaxEvent.Nothing -> Unit
                is IncomeTaxEvent.ShowError -> {
                    hideLoading()
                    adapter.update(arrayListOf())
                    DialogHelper.showErrorDialog(this, event.message)
                }
            }
        }
    }

    private fun success(incomesTax: MutableList<IncomeTaxItemModel>) {
        with(adapter) {
            update(incomesTax)
            setOnClickListener = { detail ->
                detail?.let { item ->
                    openBrowser("https://docs.google.com/gview?embedded=true&url=${item.link.orEmpty()}")
                }
            }
        }
    }

    private fun showLoading() {
        with(binding) {
            scrollViewIncomeTax.alpha = .1F
            progressBarIncomeTax.visibility = View.VISIBLE
        }
    }

    private fun hideLoading() {
        with(binding) {
            scrollViewIncomeTax.alpha = 1F
            progressBarIncomeTax.visibility = View.GONE
        }
    }
}