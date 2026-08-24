package br.com.policlinsaude.forgotPassword.presenter

import br.com.policlinsaude.domain.usecase.RecoverPasswordUseCase
import br.com.policlinsaude.domain.usecase.requestvalues.RecoverPasswordRV
import br.com.policlinsaude.core.helper.UseCaseHandler
import br.com.policlinsaude.forgotPassword.navigator.ForgotPasswordNavigator
import br.com.policlinsaude.forgotPassword.view.ForgotPasswordView
import io.reactivex.rxkotlin.subscribeBy

class ForgotPasswordPresenterImpl(private val navigator: ForgotPasswordNavigator,
                         private val recoverPasswordUseCase: RecoverPasswordUseCase,
                         private val view: ForgotPasswordView) : ForgotPasswordPresenter {

    override fun clickedButtonRecoverPassword() {
        val doLoginRV = RecoverPasswordRV(register = view.getRegister(),
                order = view.getOrder(),
                email = view.getEmail())
        UseCaseHandler.execute(recoverPasswordUseCase, doLoginRV)
                .doOnSubscribe {
                    view.showLoading()
                    view.hideButtonRecoverPassword()
                }
                .doOnTerminate {
                    view.hideLoading()
                    view.showButtonRecoverPassword()
                }
                .subscribeBy(
                        onComplete = {
                            view.showEmailSentDialog()
                        },
                        onError = {
                            view.showDialogError(it)
                        }
                )
    }

    override fun clickedButtonBack() {
        navigator.finishScreen()
    }

    override fun onEmailSentDialogOkClicked() {
        navigator.finishScreen()
    }
}