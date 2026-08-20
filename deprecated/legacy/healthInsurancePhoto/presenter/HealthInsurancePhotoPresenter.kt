package br.com.policlinsaude.ui.legacy.healthInsurancePhoto.presenter

import android.content.Context

interface HealthInsurancePhotoPresenter {
    fun getImage(context: Context)
    fun onShowBackImageClicked(photo: String?)
}