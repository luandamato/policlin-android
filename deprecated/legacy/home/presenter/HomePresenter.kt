package br.com.policlinsaude.ui.legacy.home.presenter

import java.io.Serializable

interface HomePresenter : Serializable {
    fun onViewAttached()
    fun onMenuClickedAsGuest()
    fun onValidateConnectedUser(registration: String, order: String)
    fun onValidateButtons()
}