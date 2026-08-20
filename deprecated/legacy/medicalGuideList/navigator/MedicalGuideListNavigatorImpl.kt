package br.com.policlinsaude.ui.legacy.medicalGuideList.navigator

import android.util.Log
import br.com.policlinsaude.map.view.MapsActivity
import br.com.policlinsaude.medicalGuideDetails.view.MedicalGuideDetailsActivity
import br.com.policlinsaude.medicalGuideList.view.MedicalGuideListActivity
import br.com.policlinsaude.model.PresentationEstablishment
import br.com.policlinsaude.model.PresentationEstablishmentLocation


class MedicalGuideListNavigatorImpl(private val activity: MedicalGuideListActivity)
    : MedicalGuideListNavigator {

    override fun goToDetails(establishment: PresentationEstablishment) {
        MedicalGuideDetailsActivity.start(activity, establishment, "MedicalGuideList")
    }

    override fun goToMaps(establishments: ArrayList<PresentationEstablishment>) {
        MapsActivity.start(activity, ArrayList(establishments.map {
            var latitudeDouble = 0.0

            if (it.latitude.isNotEmpty()) {
                latitudeDouble = it.latitude.replace(",", ".").toDouble()
            }
            var longitudeDouble = 0.0
            if (it.longitude.isNotEmpty()) {
                longitudeDouble = it.longitude.replace(",", ".").toDouble()
            }
            Log.d("PRESENTATION","MedicalGuideListNavigatorImpl - dentro de goToMaps")

            PresentationEstablishmentLocation(
                    title = it.title,
                    latitude = latitudeDouble,
                    longitude = longitudeDouble)
        }), true)
    }
}