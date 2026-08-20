package br.com.policlinsaude.ui.legacy.medicalGuideList.navigator

import br.com.policlinsaude.model.PresentationEstablishment

interface MedicalGuideListNavigator {
    fun goToDetails(establishment: PresentationEstablishment)
    fun goToMaps(establishments: ArrayList<PresentationEstablishment>)
}