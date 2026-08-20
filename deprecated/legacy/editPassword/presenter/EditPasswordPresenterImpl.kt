package br.com.policlinsaude.ui.legacy.editPassword.presenter

import br.com.domain.usecase.EditPasswordUseCase
import br.com.domain.usecase.requestvalues.EditPasswordRV
import br.com.policlinsaude.core.helper.UseCaseHandler
import br.com.policlinsaude.editPassword.view.EditPasswordView
import io.reactivex.rxkotlin.subscribeBy

class EditPasswordPresenterImpl(private val view: EditPasswordView,
                                private val editPasswordUseCase: EditPasswordUseCase) : EditPasswordPresenter {

    override fun onSendClicked(password: String) {
        UseCaseHandler.execute(editPasswordUseCase, EditPasswordRV(password))
                .doOnSubscribe {
                    view.showLoading()
                }
                .doOnTerminate {
                    view.hideLoading()
                }
                .subscribeBy(
                        onComplete = {
                            view.showSuccessMessage()
                        },
                        onError = {
                            view.showErrorDialog(it)
                        }
                )
    }

    override fun onSuccessDialogDismissed() {
        view.closeView()
    }
}