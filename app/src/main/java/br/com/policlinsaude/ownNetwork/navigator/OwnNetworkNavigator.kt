package br.com.policlinsaude.ownNetwork.navigator

import br.com.policlinsaude.model.PresentationEstablishment

interface OwnNetworkNavigator {
    fun goToDetails(establishment: PresentationEstablishment)
    fun goToMaps(establishments: ArrayList<PresentationEstablishment>)
}