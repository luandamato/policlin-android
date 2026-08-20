package br.com.policlinsaude.ui.legacy.medicalGuideList.presenter

import br.com.policlinsaude.medicalGuideOptions.presenter.model.PresentationLocation
import br.com.policlinsaude.model.*

interface MedicalGuideListPresenter {
    fun getMedicalGuideList(presentationPlanOptions: PresentationPlanOptions,
                            presentationCityOptions: PresentationCityOptions,
                            presentationSpecialityServiceOptions: PresentationSpecialityServiceOptions,
                            presentationProfessionalClass: PresentationProfessionalClass,
                            presentationServiceType: PresentationServiceType,
                            presentationEstablishment: PresentationEstablishmentType,
                            address_filter: String, neighborhood_filter: String, zipcode_filter: String, number_on_the_board_filter: String,//Andre
                            prof_fantasy_filter: String, cnpj_filter: String, phones_filter: String, qualificationsSearch: String?, //Andre
                            presentationLocation: PresentationLocation?)
    fun onItemClick(establishment: PresentationEstablishment)
    fun onMapClicked()
    fun onEmptyDialogOkClicked()
}