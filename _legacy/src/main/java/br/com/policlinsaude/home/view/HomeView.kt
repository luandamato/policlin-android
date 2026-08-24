package br.com.policlinsaude.home.view

import br.com.policlinsaude.domain.model.Banner
import br.com.policlinsaude.domain.model.Person
import br.com.policlinsaude.domain.model.UserConnected
import br.com.policlinsaude.domain.model.ValidateButtons

/**
 * Created by lmiyagi on 3/23/18.
 */
interface HomeView {
    fun showLoginDialog()
    fun renderPerson(person: Person)
    fun showUserNotConnectedDialog(item: UserConnected)
    fun renderEmptyBanners()
    fun renderBanners(banners: List<Banner>)
    fun showBannerLoading()
    fun hideBannerLoading()
    fun showButtons(buttons: ValidateButtons)
    fun showUpdateDialog(throwable: Throwable, force: Boolean)
}