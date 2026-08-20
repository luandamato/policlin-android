package br.com.policlinsaude.ui.legacy.ownNetwork.presenter

interface OwnNetworkPresenter {
    fun getOwnNetworks()
    fun onMapClicked(selectedTabIndex: Int)
}