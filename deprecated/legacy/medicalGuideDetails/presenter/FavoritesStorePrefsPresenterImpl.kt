package br.com.policlinsaude.ui.legacy.medicalGuideDetails.presenter

import android.util.Log
import br.com.domain.usecase.GetFavoritesUseCase
import br.com.policlinsaude.core.helper.UseCaseHandler
import br.com.policlinsaude.mapper.PresentationMedicalGuideListMapper
import br.com.policlinsaude.medicalGuideDetails.view.MedicalGuideDetailsView
import br.com.policlinsaude.model.PresentationEstablishment
import io.reactivex.rxkotlin.subscribeBy

class FavoritesStorePrefsPresenterImpl(private val view: MedicalGuideDetailsView,
                                       private val getFavoritesUseCase: GetFavoritesUseCase) : FavoritesStorePrefsPresenter {

    private var establishments = ArrayList<PresentationEstablishment>()

    override fun getFavorites() {
        Log.d("FAVORITOS", "EM getFavorites ")

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
                            Log.d("FAVORITOS", "DENTRO DE FAVORITEsSTORE - lista: " + establishments.toString())
                            view.saveFavoritesInPrefs(it)
                        },
                        onError = {
                            it.printStackTrace()
                          //  view.showDialogError(it, { getFavorites() })
                        }
                )
    }


}