package br.com.policlinsaude.ui.legacy.perfil.presenter

import br.com.domain.usecase.GetCurrentPersonUseCase
import br.com.domain.usecase.UpdateAvatarUseCase
import br.com.domain.usecase.requestvalues.UpdateAvatarRV
import br.com.policlinsaude.core.helper.UseCaseHandler
import br.com.policlinsaude.mapper.PresentationPersonMapper
import br.com.policlinsaude.perfil.navigator.PerfilNavigator
import br.com.policlinsaude.perfil.view.PerfilView
import io.reactivex.rxkotlin.subscribeBy

class PerfilPresenterImpl(private val getCurrentPersonUseCase: GetCurrentPersonUseCase,
                          private val view: PerfilView,
                          private val navigator: PerfilNavigator,
                          private val updateAvatarUseCase: UpdateAvatarUseCase) : PerfilPresenter {

    override fun getPerfil() {
        UseCaseHandler.execute(getCurrentPersonUseCase)
                .doOnSubscribe {
                    view.showLoading()
                }
                .map(PresentationPersonMapper::transform)
                .subscribeBy(
                        onNext = {
                            view.set(it)
                            view.hideLoading()
                        },
                        onError = {
                            it.printStackTrace()
                            view.showDialogError(it)
                        }
                )
    }

    override fun onAvatarChangeClicked() {
        navigator.goToImagePicker()
    }

    override fun onImagePicked(imageString: String?) {
        if (imageString.isNullOrEmpty()) {
            view.showImagePickError()
        } else {
            UseCaseHandler.execute(updateAvatarUseCase, UpdateAvatarRV(imageString!!))
                    .doOnSubscribe {
                        view.showLoading()
                    }
                    .doOnTerminate {
                        view.hideLoading()
                    }
                    .subscribeBy(
                            onComplete = {
                                view.showAvatarChangedSuccess()
                                getPerfil()
                            },
                            onError = {
                                it.printStackTrace()
                                view.showDialogError(it)
                            }
                    )
        }
    }

    override fun onImagePickError() {
        view.showImagePickError()
    }

    override fun onEditPhoneClicked() {
        navigator.goToEditPhone()
    }

    override fun onEditPasswordClicked() {
        navigator.goToEditPassword()
    }

    override fun onDeleteClicked() {
        navigator.goToDelete()
    }
}
