package br.com.policlinsaude.ui.legacy.medicalGuideOptions.presenter

import br.com.domain.helper.InvalidData
import br.com.domain.model.Establishment
import br.com.domain.model.ProfessionalClassOptions
import br.com.domain.model.ServiceTypeOptions
import br.com.policlinsaude.R
import br.com.policlinsaude.core.helper.IntentHelper
import br.com.policlinsaude.medicalGuideOptions.presenter.model.PresentationLocation
import br.com.policlinsaude.model.*
import kotlinx.android.synthetic.main.view_filters.*

interface MedicalGuideOptionsPresenter {
    fun getMedicalGuideOptions()
    fun getLocationPreference()
    fun clickedButtonSearch(plan: PresentationPlanOptions, city: PresentationCityOptions,
                            speciality: PresentationSpecialityServiceOptions,
                            orderByDistance: Boolean, professionalClass: PresentationProfessionalClass, //Andre
                            serviceType: PresentationServiceType, establishmentType: PresentationEstablishmentType,//Andre
                            address_filter: String?, neighborhood_filter: String?, zipcode_filter: String?, number_on_the_board_filter: String?,//Andre
                            prof_fantasy_filter: String?, cnpj_filter: String?, phones_filter: String?, qualificationsSearch: String?)//Andre

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