package br.com.policlinsaude.ui.legacy.home.presenter

import java.io.Serializable

/**
 * Created by lmiyagi on 3/19/18.
 */
interface MenuPresenter : Serializable {

    fun getCurrentPerson()
    fun onMenuClickedAsGuest()
    fun onLoginClicked()
    fun onLogoutConfirmed()
}