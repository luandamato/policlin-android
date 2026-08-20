package br.com.policlinsaude.map.presenter

import br.com.policlinsaude.core.helper.LocationHelper
import br.com.policlinsaude.map.navigator.MapsNavigator
import br.com.policlinsaude.map.view.MapsView
import br.com.policlinsaude.medicalGuideOptions.presenter.model.PresentationLocation
import br.com.policlinsaude.model.PresentationEstablishmentLocation
import java.util.concurrent.TimeoutException

/**
 * Created by lmiyagi on 05/04/18.
 */
class MapsPresenterImpl(private val view: MapsView,
                        private val navigator: MapsNavigator) : MapsPresenter {

    private var establishmentsLocations: ArrayList<PresentationEstablishmentLocation>? = null

    override fun onMapReady(establishmentsLocations: ArrayList<PresentationEstablishmentLocation>) {
        this.establishmentsLocations = establishmentsLocations
        view.showLoading()
        view.askForPermissions()

    }

    override fun onLocationFetched(location: PresentationLocation) {
        view.hideLoading()
        view.setupMap(location, establishmentsLocations ?: ArrayList())
    }

    override fun onLocationError(error: Throwable) {
        error.printStackTrace()
        when (error) {
            is LocationHelper.LocationNotEnabledException -> view.showLocationNotEnabledDialog()
            is TimeoutException -> view.showLocationTimeoutDialog()
            else -> view.showDialogError(error)
        }
    }

    override fun onPermissionsGranted() {
        view.getCurrentLocation()

    }

    override fun onPermissionsDenied(isAnyPermissionPermanentlyDenied: Boolean) {
        view.showOnPermissionDeniedDialog(isAnyPermissionPermanentlyDenied)
    }

    override fun onPermissionsNeededDialogOkClicked(isAnyPermissionPermanentlyDenied: Boolean) {
        if (isAnyPermissionPermanentlyDenied) {
            navigator.goToSettings()
        } else {
            view.askForPermissions()
        }
    }

    override fun onPermissionsNeededDialogCanceled() {
        view.closeView()
    }

    override fun onLocationNotEnabledDialogOkClicked() {
        navigator.goToLocationSettings()
    }

    override fun onLocationNotEnabledDialogCancelClicked() {
        view.closeView()
    }
}