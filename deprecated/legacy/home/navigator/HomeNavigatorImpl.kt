package br.com.policlinsaude.ui.legacy.home.navigator

import android.app.Activity
import br.com.policlinsaude.favorites.view.FavoritesActivity
import br.com.policlinsaude.healthInsurancePhoto.view.HealthInsurancePhotoActivity
import br.com.policlinsaude.home.view.HomeFragment
import br.com.policlinsaude.medicalGuideOptions.view.MedicalGuideOptionsActivity
import br.com.policlinsaude.ownNetwork.view.OwnNetworkActivity

class HomeNavigatorImpl(private val homeFragment: HomeFragment) : HomeNavigator {

    override fun goToOwnNetwork() {
        OwnNetworkActivity.start(homeFragment.activity as Activity)
    }

    override fun goToMedicalGuideOptions() {
        MedicalGuideOptionsActivity.start(homeFragment.activity as Activity)
    }

    override fun goToHealthInsurancePhoto() {
        HealthInsurancePhotoActivity.start(homeFragment.activity as Activity)
    }

    override fun goToFavorites() {
        FavoritesActivity.start(homeFragment.activity as Activity)
    }
}