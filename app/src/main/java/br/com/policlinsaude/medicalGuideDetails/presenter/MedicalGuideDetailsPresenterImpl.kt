package br.com.policlinsaude.medicalGuideDetails.presenter

import android.content.Context
import android.util.Log
import br.com.data.datasource.networking.CheckInternetConnection
import br.com.data.exception.PreferenceNotFoundException
import br.com.policlinsaude.domain.usecase.AddToFavoriteUseCase
import br.com.policlinsaude.domain.usecase.GetPlansUseCase
import br.com.policlinsaude.domain.usecase.RemoveFromFavoritesUseCase
import br.com.policlinsaude.domain.usecase.requestvalues.AddToFavoriteRV
import br.com.policlinsaude.domain.usecase.requestvalues.GetPlansRV
import br.com.policlinsaude.domain.usecase.requestvalues.RemoveFromFavoritesRV
import br.com.policlinsaude.core.helper.UseCaseHandler
import br.com.policlinsaude.mapper.PresentationMedicalGuideListMapper
import br.com.policlinsaude.medicalGuideDetails.navigator.MedicalGuideDetailsNavigator
import br.com.policlinsaude.medicalGuideDetails.view.MedicalGuideDetailsView
import br.com.policlinsaude.model.PresentationEstablishment
import io.reactivex.rxkotlin.subscribeBy

class MedicalGuideDetailsPresenterImpl(private val navigator: MedicalGuideDetailsNavigator,
                                       private val getPlansUseCase: GetPlansUseCase,
                                       private val addToFavoriteUseCase: AddToFavoriteUseCase,
                                       private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase,
                                       private val view: MedicalGuideDetailsView)
    : MedicalGuideDetailsPresenter {

    private var establishment: PresentationEstablishment? = null

    override fun setEstablishment(establishment: PresentationEstablishment) {
        this.establishment = establishment

        Log.d("FAVORITOS","valor de Establishment Cidade: "+ establishment.city + "  favoritado == " + establishment.favorited)

        view.setFavorited(establishment.favorited)
        view.showMedicalGuideDetails(establishment)
    }

    override fun onPhoneClicked() {
        establishment?.let {
            if (it.phoneTwo.isNotEmpty()) {
                view.showSelectPhones(it.phoneOne, it.phoneTwo)
            } else {
                navigator.goToCallIntent(it.phoneOne)
            }
        }
    }

    override fun onWhatsClicked() {
        establishment?.let {
            when {
                it.typePhoneOne == "2" && it.typePhoneTwo == "2" ->  view.showSelectWhats(it.phoneOne, it.phoneTwo)
                it.typePhoneOne == "2" -> navigator.goToWhatsApp(it.phoneOne)
                it.typePhoneTwo == "2" -> navigator.goToWhatsApp(it.phoneTwo)
            }
        }
    }
    override fun onWhatsClicked(phone: String) {
        navigator.goToWhatsApp(phone)
    }

    override fun onMapClicked() {
        establishment?.let {
            navigator.goToMapIntent(it.latitude, it.longitude, it.name)
        }
    }

    override fun onShareClicked() {
        establishment?.let {
            navigator.goToShareIntent(it)
        }
    }

    override fun onPlansClicked() {
        establishment?.let {
            UseCaseHandler.execute(getPlansUseCase, GetPlansRV(it.proUf, it.prsCod, it.proCls, it.proCod))
                    .doOnSubscribe {
                        view.showPlansLoading()
                    }
                    .doOnTerminate {
                        view.hidePlansLoading()
                    }
                    .subscribeBy(onNext = {
                        if (it.isEmpty()) {
                            view.showEmptyPlansDialog()
                        } else {
                            view.showPlansDialog(it)
                        }
                    }, onError = {
                        view.showDialogError(it)
                    })
        }
    }

    override fun onFavoriteClicked(context: Context) {

        if (CheckInternetConnection.check(context)) {

            Log.d("DETAILS", "onFavoriteClicked")
            establishment?.let {
                if (it.favorited) {
                    removeFromFavorites(it)
                } else {
                    addToFavorites(it)
                }

            }
        }else
            view.showWithoutNetworkDialog()
    }

    private fun removeFromFavorites(establishment: PresentationEstablishment) {
        UseCaseHandler.execute(removeFromFavoritesUseCase, RemoveFromFavoritesRV(PresentationMedicalGuideListMapper.transform(establishment)))
                .doOnSubscribe {
                    view.showLoading()
                }
                .doOnTerminate {
                    view.hideLoading()
                }
                .subscribeBy(
                        onComplete = {
                            establishment.favorited = false
                            view.setFavorited(false)
                            view.execGetFavorites()//
                            view.showRemoveFavoriteSuccessMessage()
                        },
                        onError = {
                            if (it is PreferenceNotFoundException) {
                                view.showLoginDialog()
                            } else {
                                view.showDialogError(it)
                            }
                        }
                )
    }

    private fun addToFavorites(establishment: PresentationEstablishment) {

        UseCaseHandler.execute(addToFavoriteUseCase, AddToFavoriteRV(PresentationMedicalGuideListMapper.transform(establishment)))
                .doOnSubscribe {
                    view.showLoading()
                }
                .doOnTerminate {
                    view.hideLoading()
                }
                .subscribeBy(
                        onComplete = {

                            establishment.favorited = true
                            view.setFavorited(true)
                            view.execGetFavorites()
                            view.showFavoriteSuccessMessage()
                        },
                        onError = {
                            if (it is PreferenceNotFoundException) {
                                view.showLoginDialog()
                            } else {
                                view.showDialogError(it)
                            }
                        }
                )
    }

    override fun onLoginClicked() {
        navigator.goToLogin()
    }

    override fun onPhoneSelected(phone: String) {
        if (phone.isNotEmpty()) navigator.goToCallIntent(phone)
    }

    override fun onWhatsSelected(phone: String) {
        if (phone.isNotEmpty()) navigator.goToWhatsApp(phone)
    }
}