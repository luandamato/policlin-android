package br.com.policlinsaude.favorites.view

import br.com.policlinsaude.model.PresentationEstablishment

/**
 * Created by lmiyagi on 3/27/18.
 */
interface FavoritesView {

    fun renderEstablishments(establishments: List<PresentationEstablishment>)
    fun showLoading()
    fun hideLoading()
    fun showDialogError(throwable: Throwable, tryAgainAction: (() -> Unit)?)
    fun showWithoutNetworkDialog()
    fun createFavoritesList(): List<PresentationEstablishment>
    fun saveFavoritesInPrefs(favorites: List<PresentationEstablishment>)
}