package br.com.policlinsaude.ui.legacy.medicalGuideOptions.navigator

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import br.com.policlinsaude.medicalGuideList.view.MedicalGuideListActivity
import br.com.policlinsaude.medicalGuideOptions.presenter.model.PresentationLocation
import br.com.policlinsaude.medicalGuideOptions.view.MedicalGuideOptionsActivity
import br.com.policlinsaude.model.*


class MedicalGuideOptionsNavigatorImpl(private val activity: MedicalGuideOptionsActivity)
    : MedicalGuideOptionsNavigator {

    override fun goToMedicalGuide(presentationPlanOptions: PresentationPlanOptions,
                                  presentationCityOptions: PresentationCityOptions,
                                  presentationSpecialityServiceOptions: PresentationSpecialityServiceOptions,
                                  location: PresentationLocation?, presentationProfessionalClass: PresentationProfessionalClass,
                                  presentationServiceType: PresentationServiceType, presentationEstablishmentType: PresentationEstablishmentType,
                                  address_filter: String?, neighborhood_filter: String?, zipcode_filter: String?, number_on_the_board_filter: String?,//Andre
                                  prof_fantasy_filter: String?, cnpj_filter: String?, phones_filter: String?, qualificationsSearch: String?)//Andre


     {

        MedicalGuideListActivity.start(activity, presentationPlanOptions, presentationCityOptions,
                presentationSpecialityServiceOptions, location, presentationProfessionalClass, presentationServiceType, presentationEstablishmentType,
                address_filter, neighborhood_filter, zipcode_filter, number_on_the_board_filter, prof_fantasy_filter, cnpj_filter, phones_filter, qualificationsSearch)
    }

    override fun goToSettings() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.data = Uri.parse("package:" + activity.packageName)
        activity.startActivity(intent)
    }

    override fun goToLocationSettings() {
       // activity.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
    }
}