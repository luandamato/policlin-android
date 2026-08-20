package br.com.policlinsaude.ui.legacy.medicalGuideOptions.navigator

import br.com.policlinsaude.medicalGuideOptions.presenter.model.PresentationLocation
import br.com.policlinsaude.model.*

interface MedicalGuideOptionsNavigator {
    fun goToMedicalGuide(presentationPlanOptions: PresentationPlanOptions,
                         presentationCityOptions: PresentationCityOptions,
                         presentationSpecialityServiceOptions: PresentationSpecialityServiceOptions,
                         location: PresentationLocation?, professionalClass: PresentationProfessionalClass,
                         serviceTypeOptions: PresentationServiceType, establishmentType: PresentationEstablishmentType,
                         address_filter: String?, neighborhood_filter: String?, zipcode_filter: String?, number_on_the_board_filter: String?,//Andre
                         prof_fantasy_filter: String?, cnpj_filter: String?, phones_filter: String?, qualificationsSearch: String?)//Andre

    fun goToSettings()
    fun goToLocationSettings()
}