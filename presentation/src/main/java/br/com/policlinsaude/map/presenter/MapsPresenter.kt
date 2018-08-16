package br.com.policlinsaude.map.presenter

import br.com.policlinsaude.medicalGuideOptions.presenter.model.PresentationLocation
import br.com.policlinsaude.model.PresentationEstablishmentLocation

/**
 * Created by lmiyagi on 05/04/18.
 */
interface MapsPresenter {
    fun onMapReady(establishments: ArrayList<PresentationEstablishmentLocation>)
    fun onLocationFetched(location: PresentationLocation)
    fun onLocationError(error: Throwable)
    fun onPermissionsGranted()
    fun onPermissionsDenied(isAnyPermissionPermanentlyDenied: Boolean)
    fun onPermissionsNeededDialogOkClicked(isAnyPermissionPermanentlyDenied: Boolean)
    fun onPermissionsNeededDialogCanceled()
    fun onLocationNotEnabledDialogOkClicked()
    fun onLocationNotEnabledDialogCancelClicked()
}