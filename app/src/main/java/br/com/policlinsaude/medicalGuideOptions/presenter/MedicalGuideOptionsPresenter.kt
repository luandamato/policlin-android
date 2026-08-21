package br.com.policlinsaude.medicalGuideOptions.presenter

import br.com.policlinsaude.medicalGuideOptions.presenter.model.PresentationLocation
import br.com.policlinsaude.model.*

interface MedicalGuideOptionsPresenter {
    fun getMedicalGuideOptions()
    fun getLocationPreference()
    fun clickedButtonSearch(plan: PresentationPlanOptions, city: PresentationCityOptions,
                            speciality: PresentationSpecialityServiceOptions,
                            orderByDistance: Boolean, professionalClass: PresentationProfessionalClass,
                            serviceType: PresentationServiceType, establishmentType: PresentationEstablishmentType,
                            address_filter: String?, neighborhood_filter: String?, zipcode_filter: String?, number_on_the_board_filter: String?,
                            prof_fantasy_filter: String?, cnpj_filter: String?, phones_filter: String?, qualificationsSearch: String?)

    fun clickedButtonCleanFields()
    fun onPermissionsGranted()
    fun onPermissionsDenied(anyPermissionPermanentlyDenied: Boolean)
    fun onPermissionsNeedDialogOkClicked(anyPermissionPermanentlyDenied: Boolean)
    fun onLocationFetched(location: PresentationLocation)
    fun onLocationFetchError()
    fun onLocationNotEnabled()
    fun onLocationNotEnabledDialogOkClicked()
    fun onOrderByDistanceChanged(checked: Boolean)
    fun onAdvancedFilterClicked(clicked: Boolean)

    fun clickedLink()
}
