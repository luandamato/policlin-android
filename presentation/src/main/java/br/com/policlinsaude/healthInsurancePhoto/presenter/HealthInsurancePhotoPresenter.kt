package br.com.policlinsaude.healthInsurancePhoto.presenter

import android.content.Context

interface HealthInsurancePhotoPresenter {
    fun getImage(context: Context)
    fun onShowBackImageClicked(photo: String?)
}