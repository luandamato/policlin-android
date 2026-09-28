package br.com.policlinsaude.ui.activities.factorExtractor

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import br.com.policlinsaude.R
import br.com.policlinsaude.databinding.ActivityFactorExtractorBinding
import br.com.policlinsaude.databinding.FragmentFactorExtractorBinding
import br.com.policlinsaude.ui.dialogs.BottomSheetCommon
import br.com.policlinsaude.ui.dialogs.DialogHelper
import br.com.policlinsaude.ui.activities.factorExtractor.FactorExtractorCardsAdapter
import br.com.policlinsaude.ui.views.BaseActivity
import br.com.policlinsaude.util.extensions.getMonthName
import br.com.policlinsaude.util.extensions.toCurrencyBRL
import br.com.policlinsaude.util.extensions.toMonthNumber
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Tela de Extrato de Coparticipação migrada para MVVM.
 *
 * Fluxo novo: UI → [FactorExtractorViewModel] → [AppRepository] → [AppService].
 */
class FactorExtractorActivity : BaseActivity() {

    companion object {
        private const val EXTRA_CO_PART_FM = "CO_PART_FM"

        fun start(activity: Activity, isCoPartFm: Boolean = false) {
            val intent = Intent(activity, FactorExtractorActivity::class.java).apply {
                putExtra(EXTRA_CO_PART_FM, isCoPartFm)
            }
            activity.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityFactorExtractorBinding
    private lateinit var contentBinding: FragmentFactorExtractorBinding

    private val viewModel: FactorExtractorViewModel by viewModel()
    private val adapter by lazy { FactorExtractorCardsAdapter(viewModel.isCoPartFm) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFactorExtractorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        contentBinding = FragmentFactorExtractorBinding.inflate(layoutInflater, binding.fragmentContainer, true)

        setSupportActionBar(binding.factorExtractorToolbar.toolbar)
        supportActionBar?.title = getString(R.string.title_factor_extractor)
        viewModel.isCoPartFm = intent.getBooleanExtra(EXTRA_CO_PART_FM, false)

        viewModel.onGetFactorsExtractorsYears()
        viewModel.onGetUser()
        setupViews()
        setupListeners()
        setupObservers()
    }

    private fun setupViews() {
        showBackButton()
        contentBinding.textviewFeEmptyFilter.text = HtmlCompat.fromHtml(
            getString(R.string.factor_extractor_empty_filter),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        showEmptyFilter()
        updateFilterButtonState()
        contentBinding.recyclerViewFeItems.adapter = adapter
    }

    private fun setupListeners() {
        contentBinding.buttonFilter.setOnClickListener {
            viewModel.onGetFactorsExtractors()
        }

        contentBinding.buttonCleanFilter.setOnClickListener {
            hideCleanButton()
            viewModel.clearMonthsSelected()
            viewModel.clearYearSelected()
            contentBinding.textviewFeYear.text = null
            contentBinding.textviewFeMonth.text = null
            showEmptyFilter()
            updateFilterButtonState()
        }

        contentBinding.textviewFeYear.setOnClickListener {
            val years = viewModel.listYears
            if (years.isEmpty()) {
                viewModel.onGetFactorsExtractorsYears()
            } else {
                bottomSheetYears(years)
            }
        }

        contentBinding.textviewFeMonth.setOnClickListener {
            val months = viewModel.listMonths
            if (months.isEmpty()) {
                viewModel.onGetFactorsExtractorsMonths()
            } else {
                bottomSheetMonths(months)
            }
        }
    }

    private fun setupObservers() {
        viewModel.loading.observe(this) { loading ->
            if (loading) showLoading() else hideLoading()
        }

        viewModel.years.observe(this) { years ->
            viewModel.listYears.clear()
            viewModel.listYears.addAll(years)
        }

        viewModel.months.observe(this) { months ->
            viewModel.listMonths.clear()
            viewModel.listMonths.addAll(months)
        }

        viewModel.extractor.observe(this) { response ->
            if (response.extratoCopart.isNullOrEmpty()) {
                DialogHelper.showErrorDialog(this, response.msgExterna.ifBlank { "Sem dados para o filtro selecionado." })
                showEmptyFilter()
            } else {
                showListData()
                contentBinding.textviewTotalValue.text = response.valorGeral?.toCurrencyBRL()
                contentBinding.textviewTotalDep.text = response.valorTotalDep?.toCurrencyBRL()
                contentBinding.textviewTotalMat.text = response.valorTotal?.toCurrencyBRL()
                adapter.update(response.extratoCopart)
                showCleanButton()
            }
            updateFilterButtonState()
        }

        viewModel.user.observe(this) { user ->
            contentBinding.lblMatricula.text = "${user.register.orEmpty()}-${user.order.orEmpty()}"
        }

        viewModel.event.observe(this) { event ->
            when (event) {
                is FactorExtractorEvent.ShowError -> DialogHelper.showErrorDialog(this, event.message)
            }
        }
    }

    private fun bottomSheetYears(years: MutableList<String>) {
        BottomSheetCommon(
            title = getString(R.string.factor_extractor_select_year),
            description = "Escolha o Ano que deseja filtrar",
            list = years,
            onClickListenerNext = { year ->
                year?.let {
                    viewModel.selectedYear = it
                    contentBinding.textviewFeYear.text = it
                    viewModel.onGetFactorsExtractorsMonths()
                    updateFilterButtonState()
                }
            },
            onClickListenerClean = {
                viewModel.clearYearSelected()
                contentBinding.textviewFeYear.text = null
                updateFilterButtonState()
            }
        ).show(supportFragmentManager, "bottom_sheet_years")
    }

    private fun bottomSheetMonths(months: MutableList<String>) {
        BottomSheetCommon(
            title = getString(R.string.factor_extractor_select_month),
            description = "Escolha o mês que deseja filtrar",
            list = months,
            onClickListenerNext = { month ->
                month?.let {
                    viewModel.selectMonth = month.toMonthNumber().toString()
                    contentBinding.textviewFeMonth.text = month
                    updateFilterButtonState()
                }
            },
            onClickListenerClean = {
                viewModel.clearMonthsSelected()
                contentBinding.textviewFeMonth.text = null
                updateFilterButtonState()
            }
        ).show(supportFragmentManager, "bottom_sheet_months")
    }

    private fun updateFilterButtonState() {
        val enabled = viewModel.enableButton()
        contentBinding.buttonFilter.isEnabled = enabled
        contentBinding.buttonFilter.setCardBackgroundColor(
            ContextCompat.getColor(
                this,
                if (enabled) R.color.Bordo else R.color.Bordo_10
            )
        )
    }

    private fun showEmptyFilter() {
        contentBinding.linearLayoutFeRecyclerView.isVisible = false
        contentBinding.cardViewTotal.isVisible = false
        contentBinding.linearLayoutFeEmptyFilter.isVisible = true
    }

    private fun showListData() {
        contentBinding.linearLayoutFeRecyclerView.isVisible = true
        contentBinding.cardViewTotal.isVisible = true
        contentBinding.linearLayoutFeEmptyFilter.isVisible = false
    }

    private fun showCleanButton() {
        contentBinding.buttonCleanFilter.isVisible = true
        contentBinding.buttonFilter.isVisible = false
    }

    private fun hideCleanButton() {
        contentBinding.buttonCleanFilter.isVisible = false
        contentBinding.buttonFilter.isVisible = true
    }

    private fun showLoading() {
        contentBinding.progressBarTickets.isVisible = true
    }

    private fun hideLoading() {
        contentBinding.progressBarTickets.isVisible = false
    }

    fun showBackButton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.factorExtractorToolbar.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}
