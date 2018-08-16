package br.com.policlinsaude.medicalGuideOptions.view

import br.com.policlinsaude.model.*

interface MedicalGuideOptionsView {
    fun showLoading()
    fun hideLoading()
    fun showMedicalGuideOptions(presentationMedicalGuideOptions: PresentationMedicalGuideOptions, userCodePlan: Int?)
    fun showDialogError(it: Throwable)
    fun selectAllSpinnerToDefault()
    fun getCurrentLocation()
    fun showNeedPermissionsDialog(anyPermissionPermanentlyDenied: Boolean)
    fun askForPermissions()
    fun showLocationMissingError()
    fun showLocationFetchErrorDialog()
    fun showLocationNotEnabledDialog()
    fun setLocationCheckbox(checked: Boolean)
    fun showAdvancedFilter(pressed: Boolean)
    //Andre
    fun showDialogFewOptions(plan: PresentationPlanOptions,
                             city: PresentationCityOptions,
                             speciality: PresentationSpecialityServiceOptions,
                             orderByDistance: Boolean,
                             professionalClass: PresentationProfessionalClass,
                             serviceType: PresentationServiceType,
                             establishmentType: PresentationEstablishmentType,
                             address_filter: String?,
                             neighborhood_filter: String?,
                             zipcode_filter: String?,
                             number_on_the_board_filter: String?,
                             prof_fantasy_fliter: String?,
                             cnpj_filter: String?,
                             phones_filter: String?,
                             qualificationsSearch: String?)

    //Andre
    fun setLocationActive(locationActive: Boolean)
    fun isGPSEnable(): Boolean

}