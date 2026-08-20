package br.com.policlinsaude.home.view

import br.com.domain.model.Person

/**
 * Created by lmiyagi on 3/19/18.
 */
interface MenuView {
    fun renderPerson(person: Person)
    fun setupGuest()
    fun showError(throwable: Throwable)
    fun showLoginDialog()
    fun showUpdateDialog(throwable: Throwable)
    fun showUserNotConnectedDialog(message: String)
}