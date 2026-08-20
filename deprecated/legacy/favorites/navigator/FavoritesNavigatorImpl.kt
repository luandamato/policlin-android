package br.com.policlinsaude.ui.legacy.favorites.navigator

import br.com.policlinsaude.favorites.view.FavoritesActivity
import br.com.policlinsaude.medicalGuideDetails.view.MedicalGuideDetailsActivity
import br.com.policlinsaude.model.PresentationEstablishment

/**
 * Created by lmiyagi on 3/27/18.
 */
class FavoritesNavigatorImpl(private val activity: FavoritesActivity) : FavoritesNavigator {

    override fun goToEstablishmentDetails(establishment: PresentationEstablishment) {
        MedicalGuideDetailsActivity.start(activity, establishment, "Favorites")
    }
}