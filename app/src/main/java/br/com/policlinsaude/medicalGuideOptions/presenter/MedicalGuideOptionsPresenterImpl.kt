package br.com.policlinsaude.medicalGuideOptions.presenter

import android.util.Log
import br.com.domain.AppConstants
import br.com.domain.model.Person
import br.com.domain.usecase.GetBooleanPreferenceUseCase
import br.com.domain.usecase.GetCurrentPersonUseCase
import br.com.domain.usecase.GetMedicalGuideOptionsUseCase
import br.com.domain.usecase.SetLocationPreferenceUseCase
import br.com.domain.usecase.requestvalues.GetBooleanPreferenceRV
import br.com.domain.usecase.requestvalues.SetLocationPreferenceRV
import br.com.policlinsaude.R
import br.com.policlinsaude.core.helper.IntentHelper
import br.com.policlinsaude.core.helper.LocationHelper
import br.com.policlinsaude.core.helper.UseCaseHandler
import br.com.policlinsaude.mapper.PresentationMedicalGuideOptionsMapper
import br.com.policlinsaude.medicalGuideOptions.navigator.MedicalGuideOptionsNavigator
import br.com.policlinsaude.medicalGuideOptions.presenter.model.PresentationLocation
import br.com.policlinsaude.medicalGuideOptions.view.MedicalGuideOptionsView
import br.com.policlinsaude.model.*
import io.reactivex.rxkotlin.subscribeBy

class MedicalGuideOptionsPresenterImpl(private val navigator: MedicalGuideOptionsNavigator,
                                       private val getCurrentPersonUseCase: GetCurrentPersonUseCase,
                                       private val getMedicalGuideOptionsUseCase: GetMedicalGuideOptionsUseCase,
                                       private val setLocationPreferenceUseCase: SetLocationPreferenceUseCase,
                                       private val getBooleanPreferenceUseCase: GetBooleanPreferenceUseCase,
                                       private val view: MedicalGuideOptionsView) : MedicalGuideOptionsPresenter {

    private var location: PresentationLocation? = null
    private var userCodePlan: Int? = null

    override fun getMedicalGuideOptions() {
        UseCaseHandler.execute(getCurrentPersonUseCase)
                .doOnSubscribe {
                    view.showLoading()
                }
                .onErrorReturnItem(Person())
                .flatMap({
                    if (it.codePlan.isNotEmpty()) {
                        userCodePlan = it.codePlan.toInt()
                    }
                    UseCaseHandler.execute(getMedicalGuideOptionsUseCase)
                            .doOnTerminate {
                                view.hideLoading()
                            }
                })
                .map(PresentationMedicalGuideOptionsMapper::transform)
                .subscribeBy(
                        onNext = {
                            view.showMedicalGuideOptions(it, userCodePlan)
                        },
                        onError = {
                            view.showDialogError(it)
                        }
                )
    }

    override fun getLocationPreference() {
        UseCaseHandler.execute(getBooleanPreferenceUseCase, GetBooleanPreferenceRV(AppConstants.PREFERENCES_LOCATION, false))
                .subscribeBy(
                        onNext = {
                            //view.setLocationCheckbox(it)
                            view.setLocationActive(it) //Andre
                        },
                        onError = {
                            it.printStackTrace()
                            view.showDialogError(it)
                        })
    }

    override fun clickedButtonSearch(plan: PresentationPlanOptions, city: PresentationCityOptions,
                                     speciality: PresentationSpecialityServiceOptions,
                                    // orderByDistance: Boolean, professionalClass: PresentationProfessionalClass,
                                     isLocationActive: Boolean, professionalClass: PresentationProfessionalClass,
                                     serviceTypeOptions: PresentationServiceType, establishmentType: PresentationEstablishmentType,
                                     address_filter: String?, neighborhood_filter: String?, zipcode_filter: String?, number_on_the_board_filter: String?,//Andre
                                     prof_fantasy_filter: String?, cnpj_filter: String?, phones_filter: String?, qualificationsSearch: String?)//Andre

     {
     //   if (orderByDistance) {
        if (isLocationActive) {
            Log.d("Andre", "isLocationActive é TRUE ")
         /*   if (!view.isGPSEnable()){//(location == null) {
                Log.d("Andre", "location é NULL e gera msg ")
             //   view.showLocationMissingError()
            } else {*/
                //view.askForPermissions()
                navigator.goToMedicalGuide(plan, city, speciality, location, professionalClass,serviceTypeOptions, establishmentType,
                        address_filter, neighborhood_filter, zipcode_filter, number_on_the_board_filter, prof_fantasy_filter, cnpj_filter,phones_filter, qualificationsSearch)
           // }
        } else {
            navigator.goToMedicalGuide(plan, city, speciality, null, professionalClass, serviceTypeOptions, establishmentType,
                        address_filter, neighborhood_filter, zipcode_filter, number_on_the_board_filter, prof_fantasy_filter, cnpj_filter,phones_filter, qualificationsSearch)
        }
    }

    override fun clickedButtonCleanFields() {
        view.selectAllSpinnerToDefault()
    }

    override fun onAdvancedFilterClicked(clicked: Boolean) {
        view.showAdvancedFilter(clicked)

    }

    override fun onPermissionsGranted() {
        view.getCurrentLocation()
    }

    override fun onPermissionsDenied(anyPermissionPermanentlyDenied: Boolean) {
        view.showNeedPermissionsDialog(anyPermissionPermanentlyDenied)
    }

    override fun onPermissionsNeedDialogOkClicked(anyPermissionPermanentlyDenied: Boolean) {
        if (anyPermissionPermanentlyDenied) {
            navigator.goToSettings()
        } else {
            view.askForPermissions()
        }
    }

    override fun onLocationFetched(location: PresentationLocation) {
        this.location = location
    }

    override fun onLocationFetchError() {
        view.showLocationFetchErrorDialog()
    }

    override fun onLocationNotEnabled() {
        view.showLocationNotEnabledDialog()
    }

    override fun onLocationNotEnabledDialogOkClicked() {
        navigator.goToLocationSettings()
    }

    override fun onOrderByDistanceChanged(checked: Boolean) {
        UseCaseHandler.execute(setLocationPreferenceUseCase, SetLocationPreferenceRV(checked))
                .subscribeBy(
                        onComplete = {
                            // do nothing
                        },
                        onError = {
                            it.printStackTrace()
                            view.showDialogError(it)
                        })
    }

    override fun clickedLink() {
        view.clickedLink()
    }
}