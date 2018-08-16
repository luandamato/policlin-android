package br.com.policlinsaude.units.presenter

import br.com.policlinsaude.model.PresentationEstablishment

interface UnitsPresenter {
    fun getUnits()
    fun onMapClicked(selectedTabIndex: Int)
    fun onItemClick(establishment: PresentationEstablishment)

}