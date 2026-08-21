package br.com.policlinsaude.healthInsurancePhoto.presenter

import android.content.Context
import android.util.Log
import br.com.policlinsaude.data.datasource.networking.CheckInternetConnection
import br.com.policlinsaude.domain.usecase.GetHealthInsurancePhotoUseCase
import br.com.policlinsaude.core.helper.UseCaseHandler
import br.com.policlinsaude.healthInsurancePhoto.navigator.HealthInsurancePhotoNavigator
import br.com.policlinsaude.healthInsurancePhoto.view.HealthInsurancePhotoView
import br.com.policlinsaude.mapper.PresentationMedicalGuideListMapper
import io.reactivex.rxkotlin.subscribeBy

class HealthInsurancePhotoPresenterImpl(private val navigator: HealthInsurancePhotoNavigator,
                                        private val getHealthInsurancePhotoUseCase: GetHealthInsurancePhotoUseCase,
                                        private val view: HealthInsurancePhotoView) : HealthInsurancePhotoPresenter {


    override fun getImage(context: Context) {

        if (CheckInternetConnection.check(context)) { // check de conexão
            UseCaseHandler.execute(getHealthInsurancePhotoUseCase)
                    .doOnSubscribe {
                        view.showLoading()
                    }
                    .doOnTerminate {
                        view.hideLoading()
                    }

                    .subscribeBy(
                            onNext = {
                                Log.d("CARTEIRINHA", "NO SUBSCRIBEBY" + it.toString())
                                view.showImage(it)
                            },
                            onError = {

                                view.showDialogError(it)
                            }
                    )
        }
        else {
               view.hideLoading()
               view.showWithoutNetworkDialog()
        }


    }

    override fun onShowBackImageClicked(photo: String?) {

        Log.d("CARTEIRINHA","FOTO DO VERSO em onShowBackImageClicked : " + photo)

        view.showImageVerse(photo)

    }


}