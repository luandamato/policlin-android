package br.com.policlinsaude.map.view

import br.com.policlinsaude.medicalGuideOptions.presenter.model.PresentationLocation
import br.com.policlinsaude.model.PresentationEstablishmentLocation

/**
 * Created by lmiyagi on 05/04/18.
 */
interface MapsView {

    fun showLoading()
    fun hideLoading()
    fun getCurrentLocation()
    fun setupMap(location: PresentationLocation, establishments: ArrayList<PresentationEstablishmentLocation>)
    fun showDialogError(error: Throwable)
    fun askForPermissions()
    fun showOnPermissionDeniedDialog(anyPermissionPermanentlyDenied: Boolean)
    fun closeView()
    fun showLocationNotEnabledDialog()
    fun showLocationTimeoutDialog()
}