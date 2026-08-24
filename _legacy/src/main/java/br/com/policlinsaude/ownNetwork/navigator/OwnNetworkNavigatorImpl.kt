package br.com.policlinsaude.ownNetwork.navigator

import br.com.policlinsaude.map.view.MapsActivity
import br.com.policlinsaude.medicalGuideDetails.view.MedicalGuideDetailsActivity
import br.com.policlinsaude.model.PresentationEstablishment
import br.com.policlinsaude.model.PresentationEstablishmentLocation
import br.com.policlinsaude.ownNetwork.view.OwnNetworkActivity

class OwnNetworkNavigatorImpl(private val activity: OwnNetworkActivity)
    : OwnNetworkNavigator {

    override fun goToDetails(establishment: PresentationEstablishment) {
        MedicalGuideDetailsActivity.start(activity, establishment, "OwnNetwork")
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

            PresentationEstablishmentLocation(
                    title = it.title,
                    latitude = latitudeDouble,
                    longitude = longitudeDouble)
        }), false)
    }
}