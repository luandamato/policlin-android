package br.com.policlinsaude.ui.legacy.ownNetwork.navigator

import br.com.policlinsaude.model.PresentationEstablishment

interface OwnNetworkNavigator {
    fun goToDetails(establishment: PresentationEstablishment)
    fun goToMaps(establishments: ArrayList<PresentationEstablishment>)
}