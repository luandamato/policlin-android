package br.com.policlinsaude.ui.fragments.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import br.com.policlinsaude.R
import br.com.policlinsaude.databinding.FragmentHomeBinding
import br.com.policlinsaude.domain.models.Banner
import br.com.policlinsaude.ui.activities.home.MenuActivity
import br.com.policlinsaude.ui.activities.login.LoginActivity
import br.com.policlinsaude.ui.activities.notifications.NotificationActivity
import br.com.policlinsaude.ui.activities.healthInsuranceCard.HealthInsurancePhotoActivity
import br.com.policlinsaude.ui.dialogs.DialogHelper
import br.com.policlinsaude.ui.views.BaseFragment
import br.com.policlinsaude.util.extensions.openBrowser
import br.com.policlinsaude.util.extensions.setupAutoscroll
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Pantalla home: tiles + banners (ViewPager2).
 * Migrado de `_legacy/.../home/view/HomeFragment.kt` (MVP → MVVM).
 */
class HomeFragment : BaseFragment() {

    private lateinit var binding: FragmentHomeBinding

    private val viewModel: HomeViewModel by viewModel()

    private lateinit var adapter: HomeAdapter

    private val bannersAdapter by lazy { HomePageAdapter(requireContext()) { onBannerClick(it) } }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFragmentToolbar()
        setupAdapter()
        observeViewModel()
        viewModel.onViewAttached()
    }

    private fun setupFragmentToolbar() {
        val activity = activity as? MenuActivity
        activity?.setupFragmentToolbar(binding.appBarContainer.toolbar, null)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupAdapter() {
        adapter = HomeAdapter(mutableListOf()) { option -> onOptionClick(option) }
        binding.recyclerView.adapter = adapter

        binding.viewPager.adapter = bannersAdapter
        binding.viewPager.setupAutoscroll(5000L)

        binding.appBarContainer.alertMenu.setOnClickListener {
            NotificationActivity.start(requireContext())
        }
    }

    private fun observeViewModel() {
        viewModel.person.observe(this) { person ->
            person?.let {
                binding.appBarContainer.personNameTextView.text =
                    getString(R.string.text_person_home_title, it.name, it.descriptionPlan)
            }
        }

        viewModel.options.observe(this) { options ->
            adapter.setup(options)
        }

        viewModel.banners.observe(this) { banners ->
            bannersAdapter.updateBanners(banners)
            updateBannerIndicator(banners.size)
        }

        viewModel.bannerLoading.observe(this) { loading ->
            binding.bannerProgress.isVisible = loading
        }

        viewModel.loading.observe(this) { loading ->
            binding.loadingContainerHome.isVisible = loading
        }

        viewModel.forceUpdate.observe(this) { force ->
            if (force) {
                showUpdateDialog("É obrigatória a atualização do app")
            }
        }

        viewModel.event.observe(this) { event ->
            when (event) {
                is HomeEvent.ShowLoginDialog -> showLoginDialog()
                is HomeEvent.ShowError -> DialogHelper.showErrorDialog(requireContext(), event.message)
                is HomeEvent.ShowUpdateApp -> showUpdateDialog(event.message)
            }
        }
    }

    // =====================================================================
    // Banners (indicador nativo de pontos)
    // =====================================================================
    private fun updateBannerIndicator(count: Int) {
        binding.bannerIndicator.removeAllViews()
        for (i in 0 until count) {
            val lp = android.widget.LinearLayout.LayoutParams(10, 10)
            lp.marginStart = 4
            lp.marginEnd = 4
            val dot = View(requireContext()).apply {
                layoutParams = lp
                setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            }
            binding.bannerIndicator.addView(dot)
        }
    }

    private fun onBannerClick(banner: Banner) {
        if (banner.url.isNotEmpty()) {
            requireContext().openBrowser(banner.url)
        }
    }

    // =====================================================================
    // Navegación (UI) — destinos aún no migrados → Toast "Em construção"
    // =====================================================================
    private fun onOptionClick(option: HomeOptionEnum) {
        if (viewModel.forceUpdate.value == true) {
            showUpdateDialog("É obrigatória a atualização do app")
            return
        }
        when (option) {
            HomeOptionEnum.HEALTH_INSURANCE -> {
                if ((activity as? MenuActivity)?.isGuestMode() == true) {
                    showLoginDialog()
                } else {
                    HealthInsurancePhotoActivity.start(requireActivity())
                }
            }
            HomeOptionEnum.MEDICAL_GUIDE,
            HomeOptionEnum.OWN_NETWORK,
            HomeOptionEnum.FAVORITES,
            HomeOptionEnum.TICKET,
            HomeOptionEnum.RESEARCH_VALUES_CO_PARTICIPATION,
            HomeOptionEnum.FACTOR_EXTRACTOR,
            HomeOptionEnum.INCOME_TAX,
            HomeOptionEnum.GUIDE_AUTHORIZER,
            HomeOptionEnum.SCHEDULE,
            HomeOptionEnum.SERVICE_TOKEN -> onProtectedOption()
        }
    }

    private fun onProtectedOption() {
        if ((activity as? MenuActivity)?.isGuestMode() == true) {
            showLoginDialog()
        } else {
            notMigrated()
        }
    }

    private fun notMigrated() {
        Toast.makeText(requireContext(), "Em construção", Toast.LENGTH_SHORT).show()
    }

    private fun showLoginDialog() {
        DialogHelper.showDialog(
            requireContext(),
            getString(R.string.title_login),
            getString(R.string.text_login),
            getString(R.string.global_yes),
            getString(R.string.action_cancel),
            listenerPositiveButton = { navigateToLogin() }
        )
    }

    private fun navigateToLogin() {
        LoginActivity.start(requireActivity())
    }

    private fun showUpdateDialog(message: String) {
        DialogHelper.showDialog(
            requireContext(),
            getString(R.string.title_error_oops),
            message,
            getString(R.string.text_ok),
            null
        )
    }
}