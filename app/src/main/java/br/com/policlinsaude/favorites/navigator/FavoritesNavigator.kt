package br.com.policlinsaude.favorites.navigator

import br.com.policlinsaude.model.PresentationEstablishment

/**
 * Created by lmiyagi on 3/27/18.
 */
interface FavoritesNavigator {
    fun goToEstablishmentDetails(establishment: PresentationEstablishment)
}