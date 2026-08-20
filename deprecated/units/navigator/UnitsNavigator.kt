package br.com.policlinsaude.units.navigator

import br.com.policlinsaude.model.PresentationEstablishment

interface UnitsNavigator {
    fun goToDetails(establishment: PresentationEstablishment)
    fun goToMaps(establishments: ArrayList<PresentationEstablishment>)

}