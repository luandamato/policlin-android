package br.com.policlinsaude.ui.legacy.preferences.presenter

import android.util.Log
import br.com.domain.AppConstants
import br.com.domain.usecase.GetBooleanPreferenceUseCase
import br.com.domain.usecase.SetLocationPreferenceUseCase
import br.com.domain.usecase.SetNotificationPreferenceUseCase
import br.com.domain.usecase.DoLogoffUseCase
import br.com.domain.usecase.requestvalues.GetBooleanPreferenceRV
import br.com.domain.usecase.requestvalues.SetLocationPreferenceRV
import br.com.domain.usecase.requestvalues.SetNotificationPreferenceRV
import br.com.policlinsaude.core.helper.UseCaseHandler
import br.com.policlinsaude.preferences.navigator.PreferencesNavigator
import br.com.policlinsaude.preferences.view.PreferencesView
import io.reactivex.rxkotlin.subscribeBy

/**
 * Created by lmiyagi on 3/26/18.
 */
class PreferencesPresenterImpl(private val view: PreferencesView,
                               private val navigator: PreferencesNavigator,
                               private val getBooleanPreferenceUseCase: GetBooleanPreferenceUseCase,
                               private val setNotificationPreferenceUseCase: SetNotificationPreferenceUseCase,
                               private val setLocationPreferenceUseCase: SetLocationPreferenceUseCase,
                               private val doLogoffUseCase: DoLogoffUseCase)
    : PreferencesPresenter {

    private var locationState: Boolean = false

    override fun onViewStarted() {
        UseCaseHandler.execute(getBooleanPreferenceUseCase, GetBooleanPreferenceRV(AppConstants.PREFERENCES_LOCATION))
                .subscribeBy(
                        onNext = {
                            locationState = it
                            view.setLocationPreference(it)
                        },
                        onError = {
                            it.printStackTrace()
                            view.showError(it)
                        }
                )
        UseCaseHandler.execute(getBooleanPreferenceUseCase, GetBooleanPreferenceRV(AppConstants.PREFERENCES_NOTIFICATION))
                .subscribeBy(
                        onNext = {
                            view.setNotificationPreference(it)
                        },
                        onError = {
                            it.printStackTrace()
                            view.showError(it)
                        }
                )
    }

    override fun onNotificationStateChanged(isChecked: Boolean) {
        UseCaseHandler.execute(setNotificationPreferenceUseCase, SetNotificationPreferenceRV(isChecked))
                .subscribeBy(
                        onError = {
                            it.printStackTrace()
                            view.showError(it)
                        }
                )
    }

    override fun onLocationStateChanged(isChecked: Boolean) {
        locationState = isChecked
        view.askForPermissions()
    }

    private fun setLocationPreference(isChecked: Boolean) {
        Log.d("PREFERENCES","Dentro de setLocationPreference Buscando no servidor")
        UseCaseHandler.execute(setLocationPreferenceUseCase, SetLocationPreferenceRV(isChecked))
                .subscribeBy(
                        onError = {
                            it.printStackTrace()
                            view.showError(it)
                        }
                )
    }

    override fun onLogoffClicked() {
        view.showLogoffConfirmDialog()
    }

    override fun onLogoutConfirmed() {
        UseCaseHandler.execute(doLogoffUseCase)
                .doOnSubscribe {
                    view.showLoading()
                }
                .subscribeBy(
                        onComplete = {
                            navigator.goToLogin()
                            view.closeView()
                        },
                        onError = {
                            it.printStackTrace()
                            view.showError(it)
                        }
                )
    }

    override fun onPermissionsGranted() {
        Log.d("PREFERENCES","Dentro de onPermissionsGranted")
        setLocationPreference(locationState)
    }

    override fun onPermissionsDenied(isAnyPermissionPermanentlyDenied: Boolean) {
        Log.d("PREFERENCES","Dentro de onPermissionsDenied")
        //view.showOnPermissionDeniedDialog(isAnyPermissionPermanentlyDenied)
        view.setLocationPreference(false)
    }

    override fun onPermissionsNeededDialogOkClicked(isAnyPermissionPermanentlyDenied: Boolean) {
        Log.d("PREFERENCES","Dentro de onPermissionsNeededDialogOkClicked")
        if (isAnyPermissionPermanentlyDenied) {
            navigator.goToSettings()
        } else {
          //  view.askForPermissions()
        }
    }

    override fun onPermissionsNeededDialogCanceled() {
        view.setLocationPreference(false)
    }
}