package br.com.policlinsaude.preferences.view

/**
 * Created by lmiyagi on 3/26/18.
 */
interface PreferencesView {
    fun showError(throwable: Throwable)
    fun setLocationPreference(checked: Boolean)
    fun setNotificationPreference(checked: Boolean)
    fun showLogoffConfirmDialog()
    fun showLoading()
    fun closeView()
    fun askForPermissions()
    fun showOnPermissionDeniedDialog(anyPermissionPermanentlyDenied: Boolean)
}