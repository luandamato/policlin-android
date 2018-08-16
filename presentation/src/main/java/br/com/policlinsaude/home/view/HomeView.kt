package br.com.policlinsaude.home.view

import br.com.domain.model.Banner
import br.com.domain.model.Person

/**
 * Created by lmiyagi on 3/23/18.
 */
interface HomeView {
    fun showLoginDialog()
    fun renderPerson(person: Person)
    fun renderEmptyBanners()
    fun renderBanners(banners: List<Banner>)
    fun showBannerLoading()
    fun hideBannerLoading()
}