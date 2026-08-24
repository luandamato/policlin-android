package br.com.policlinsaude.editPhone.presenter

import br.com.policlinsaude.domain.usecase.UpdateEmailUseCase
import br.com.policlinsaude.domain.usecase.requestvalues.UpdatePhoneRV
import br.com.policlinsaude.core.helper.UseCaseHandler
import br.com.policlinsaude.editPhone.view.EditPhoneView
import io.reactivex.rxkotlin.subscribeBy

class EditPhonePresenterImpl(private val view: EditPhoneView,
                             private val updateEmailUseCase: UpdateEmailUseCase)
    : EditPhonePresenter {

    override fun onSendClicked(phone: String) {
        UseCaseHandler.execute(updateEmailUseCase, UpdatePhoneRV(getCodeArea(phone), getPhoneWithoutCodeArea(phone)))
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
                            it.printStackTrace()
                            view.showErrorDialog(it)
                        }
                )
    }

    override fun onSuccessDialogDismissed() {
        view.closeView()
    }

    private fun getCodeArea(phone: String): String {
        if (phone.length < 2) return ""
        return phone.substring(0, 2)
    }

    private fun getPhoneWithoutCodeArea(phone: String): String {
        if (phone.length < 2) return ""
        return phone.substring(2)
    }
}