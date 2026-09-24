package br.com.policlinsaude.ui.fragments.preferences

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import br.com.policlinsaude.R
import br.com.policlinsaude.databinding.FragmentPreferencesBinding
import br.com.policlinsaude.ui.activities.login.LoginActivity
import br.com.policlinsaude.ui.activities.home.MenuActivity
import br.com.policlinsaude.ui.dialogs.DialogHelper
import br.com.policlinsaude.ui.views.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Fragment de Preferências migrado para MVVM com Koin.
 *
 * Gerencia as configurações de notificações e localização do usuário,
 * além de permitir a realização de logoff.
 */
class PreferencesFragment : Fragment() {

    private val viewModel: PreferencesViewModel by viewModel()
    private var _binding: FragmentPreferencesBinding? = null
    private val binding get() = _binding!!

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.entries.all { it.value }
        if (granted) {
            viewModel.onPermissionsGranted()
        } else {
            viewModel.onPermissionsDenied()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreferencesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupViews()
        setupObservers()
    }

    private fun setupToolbar() {
        val toolbar = binding.toolbar.toolbar
        (activity as? MenuActivity)?.setupFragmentToolbar(toolbar, R.string.title_preferences)
    }

    private fun setupViews() {
        // Usamos setOnClickListener em vez de setOnCheckedChangeListener para evitar loops
        // infinitos quando o LiveData atualiza o estado do Switch programaticamente.
        binding.notificationSwitch.setOnClickListener {
            viewModel.onNotificationStateChanged(binding.notificationSwitch.isChecked)
        }

        binding.locationSwitch.setOnClickListener {
            viewModel.onLocationStateChanged(binding.locationSwitch.isChecked)
        }

        binding.logoffButton.setOnClickListener {
            viewModel.onLogoffClicked()
        }
    }

    private fun setupObservers() {
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.loadingView.root.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.notificationEnabled.observe(viewLifecycleOwner) { isEnabled ->
            if (binding.notificationSwitch.isChecked != isEnabled) {
                binding.notificationSwitch.isChecked = isEnabled
            }
        }

        viewModel.locationEnabled.observe(viewLifecycleOwner) { isEnabled ->
            if (binding.locationSwitch.isChecked != isEnabled) {
                binding.locationSwitch.isChecked = isEnabled
            }
        }

        viewModel.event.observe(viewLifecycleOwner) { event ->
            when (event) {
                is PreferencesEvent.ShowLogoffConfirmation -> showLogoffConfirmDialog()
                is PreferencesEvent.LogoffSuccess -> navigateToLogin()
                is PreferencesEvent.AskForPermissions -> askForLocationPermissions()
                is PreferencesEvent.ShowError -> {
                    (activity as? BaseActivity)?.showError(message = event.message)
                }
            }
        }
    }

    private fun askForLocationPermissions() {
        val fineLocation = Manifest.permission.ACCESS_FINE_LOCATION
        val coarseLocation = Manifest.permission.ACCESS_COARSE_LOCATION

        val hasFine = ContextCompat.checkSelfPermission(requireContext(), fineLocation) == PackageManager.PERMISSION_GRANTED
        val hasCoarse = ContextCompat.checkSelfPermission(requireContext(), coarseLocation) == PackageManager.PERMISSION_GRANTED

        if (hasFine && hasCoarse) {
            viewModel.onPermissionsGranted()
        } else {
            requestPermissionLauncher.launch(arrayOf(fineLocation, coarseLocation))
        }
    }

    private fun showLogoffConfirmDialog() {
        DialogHelper.showDialog(
            context = requireContext(),
            title = getString(R.string.title_logoff),
            message = getString(R.string.text_logoff_confirmation),
            messagePositiveButton = getString(R.string.global_yes),
            messageNegativeButton = getString(R.string.action_cancel),
            listenerPositiveButton = { viewModel.onLogoutConfirmed() }
        )
    }

    private fun navigateToLogin() {
        val intent = Intent(requireContext(), LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        activity?.finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): PreferencesFragment = PreferencesFragment()
    }
}
