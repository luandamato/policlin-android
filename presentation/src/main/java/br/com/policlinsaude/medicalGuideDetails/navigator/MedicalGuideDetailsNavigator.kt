package br.com.policlinsaude.medicalGuideDetails.navigator

import br.com.policlinsaude.model.PresentationEstablishment


interface MedicalGuideDetailsNavigator {
    fun goToCallIntent(phone: String)
    fun goToShareIntent(establishment: PresentationEstablishment)
    fun goToMapIntent(lat: String, lng: String, name: String)
    fun goToLogin()
}