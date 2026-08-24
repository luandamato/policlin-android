package br.com.policlinsaude.favorites.presenter

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import br.com.policlinsaude.data.datasource.networking.CheckInternetConnection
import br.com.policlinsaude.domain.usecase.GetFavoritesUseCase
import br.com.policlinsaude.core.helper.UseCaseHandler
import br.com.policlinsaude.favorites.navigator.FavoritesNavigator
import br.com.policlinsaude.favorites.view.FavoritesView
import br.com.policlinsaude.mapper.PresentationMedicalGuideListMapper
import br.com.policlinsaude.model.PresentationEstablishment
import io.reactivex.rxkotlin.subscribeBy

/**
 * Created by lmiyagi on 3/27/18.
 */
class FavoritesPresenterImpl(private val view: FavoritesView,
                             private val navigator: FavoritesNavigator,
                             private val getFavoritesUseCase: GetFavoritesUseCase) : FavoritesPresenter {

    private var establishments = ArrayList<PresentationEstablishment>()

    override fun getFavorites(context: Context) {

        if (CheckInternetConnection.check(context)) { // check de conexão

            UseCaseHandler.execute(getFavoritesUseCase, null)
                    .doOnSubscribe {
                        view.showLoading()
                    }
                    .doOnTerminate {
                        view.hideLoading()
                    }
                    .map({ PresentationMedicalGuideListMapper.transform(it.toTypedArray()) })
                    .subscribeBy(
                            onNext = {
                                this.establishments.clear()
                                this.establishments.addAll(it)
                                view.saveFavoritesInPrefs(it)
                                view.renderEstablishments(it)
                            },
                            onError = {
                                it.printStackTrace()
                                view.showDialogError(it, { getFavorites(context) })
                            }
                    )
        }
       else {

           // val prefMgr: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
           // prefMgr.getString(FAVO)

            view.showWithoutNetworkDialog()
            if (view.createFavoritesList().size > 0) {

                this.establishments.clear()
                this.establishments.addAll(view.createFavoritesList())
            }


        }
    }

    override fun onEstablishmentClicked(establishmentIndex: Int) {
        Log.d("FAVORITOS","onEstablishmentClicked-- indice selecionado: --> " + establishmentIndex)
        val establishment = establishments[establishmentIndex]
        establishment.favorited = true
        navigator.goToEstablishmentDetails(establishment)
    }
}