package br.com.policlinsaude.home.presenter

import java.io.Serializable

/**
 * Created by lmiyagi on 3/19/18.
 */
interface MenuPresenter : Serializable {

    fun getCurrentPerson()
    fun onMenuClickedAsGuest()
    fun onLoginClicked()
}