package br.com.policlinsaude.healthInsurancePhoto.view

import br.com.policlinsaude.domain.model.HealthInsurancePhoto
import br.com.policlinsaude.domain.model.HealthInsurancePhotoList

interface HealthInsurancePhotoView {
    fun showLoading()
    fun hideLoading()
    //fun showImage(photo: String)
    fun showImage(photoListFull: HealthInsurancePhotoList)
    fun showDialogError(it: Throwable)
    fun showImageVerse(photo: String?)
    fun showWithoutNetworkDialog()
}