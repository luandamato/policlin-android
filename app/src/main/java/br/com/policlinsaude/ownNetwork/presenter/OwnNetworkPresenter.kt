package br.com.policlinsaude.ownNetwork.presenter

interface OwnNetworkPresenter {
    fun getOwnNetworks()
    fun onMapClicked(selectedTabIndex: Int)
}