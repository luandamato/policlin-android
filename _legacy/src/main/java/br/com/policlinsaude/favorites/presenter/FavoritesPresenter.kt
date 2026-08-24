package br.com.policlinsaude.favorites.presenter

import android.content.Context

/**
 * Created by lmiyagi on 3/27/18.
 */
interface FavoritesPresenter {
    fun getFavorites(context: Context)
    fun onEstablishmentClicked(establishmentIndex: Int)
}