package br.com.policlinsaude.preferences.view

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import br.com.policlinsaude.R
import br.com.policlinsaude.core.base.BaseFragmentWithInject
import br.com.policlinsaude.core.helper.DialogHelper
import br.com.policlinsaude.databinding.FragmentPreferencesBinding
import br.com.policlinsaude.home.view.MenuActivity
import br.com.policlinsaude.preferences.presenter.PreferencesPresenter
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener
import javax.inject.Inject

class PreferencesFragment : BaseFragmentWithInject(), PreferencesView {

    companion object {
        fun newInstance(): PreferencesFragment {
            return PreferencesFragment()
        }
    }

    @Inject
    lateinit var presenter: PreferencesPresenter

    private var _binding: FragmentPreferencesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentPreferencesBinding.inflate(inflater, container, false)

        val toolbar: Toolbar = binding.toolbar.toolbar
        (activity as MenuActivity).setupFragmentToolbar(toolbar, R.string.title_preferences)

        presenter.onViewStarted()
        setupView()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun showError(throwable: Throwable) {
        (activity as MenuActivity).showError(throwable)
    }

    override fun setLocationPreference(checked: Boolean) {
        binding.locationSwitch.isChecked = checked
    }

    override fun setNotificationPreference(checked: Boolean) {
        binding.notificationSwitch.isChecked = checked
    }

    override fun showLogoffConfirmDialog() {
        context?.let {
            DialogHelper.showDialog(it,
                R.string.title_logoff,
                R.string.text_logoff_confirmation,
                R.string.global_yes,
                R.string.action_cancel,
                { presenter.onLogoutConfirmed() })
        }
    }

    override fun showLoading() {
        binding.loadingView.root.visibility = View.VISIBLE
    }

    override fun closeView() {
        activity?.finish()
    }

    override fun askForPermissions() {
        Dexter.withActivity(requireActivity())
            .withPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            .withListener(object : BaseMultiplePermissionsListener() {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if (it.areAllPermissionsGranted()) {
                            Log.d("PREFERENCES", "Dentro de onPermissionsChecked/areAllPermissionsGranted em askForPermissions na ACTIVITY ")
                            presenter.onPermissionsGranted()
                        } else {
                            Log.d("PREFERENCES", "Dentro de onPermissionsChecked/onPermissionsDenied em askForPermissions na ACTIVITY ")
                            presenter.onPermissionsDenied(it.isAnyPermissionPermanentlyDenied)
                        }
                    }
                }
            })
            .check()
    }

    override fun showOnPermissionDeniedDialog(anyPermissionPermanentlyDenied: Boolean) {
        DialogHelper.showDialog(requireContext(),
            R.string.title_permissions_needed,
            R.string.text_permissions_needed,
            R.string.text_ok,
            listenerNegativeButton = { presenter.onPermissionsNeededDialogCanceled() },
            listenerPositiveButton = { presenter.onPermissionsNeededDialogOkClicked(anyPermissionPermanentlyDenied) },
            onDismiss = { presenter.onPermissionsNeededDialogOkClicked(anyPermissionPermanentlyDenied) })
    }

    private fun setupView() {
        binding.notificationSwitch.setOnCheckedChangeListener { _, checked ->
            presenter.onNotificationStateChanged(checked)
        }
        binding.locationSwitch.setOnCheckedChangeListener { _, checked ->
            presenter.onLocationStateChanged(checked)
        }
        binding.logoffButton.setOnClickListener {
            presenter.onLogoffClicked()
        }
    }
}
