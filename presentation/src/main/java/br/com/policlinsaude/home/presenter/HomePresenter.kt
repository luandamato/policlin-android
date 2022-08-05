package br.com.policlinsaude.home.presenter

import java.io.Serializable

interface HomePresenter : Serializable {
    fun onViewAttached()
    fun onMenuClickedAsGuest()
    fun onValidateConnectedUser(registration: String, order: String)
}