package br.com.policlinsaude.ui.legacy.preferences.presenter

/**
 * Created by lmiyagi on 3/26/18.
 */
interface PreferencesPresenter {

    fun onViewStarted()
    fun onNotificationStateChanged(isChecked: Boolean)
    fun onLocationStateChanged(isChecked: Boolean)
    fun onLogoffClicked()
    fun onLogoutConfirmed()
    fun onPermissionsGranted()
    fun onPermissionsDenied(isAnyPermissionPermanentlyDenied: Boolean)
    fun onPermissionsNeededDialogOkClicked(isAnyPermissionPermanentlyDenied: Boolean)
    fun onPermissionsNeededDialogCanceled()
}