package br.com.policlinsaude.login.presenter

import br.com.data.exception.PreferenceNotFoundException
import br.com.data.exception.RealmNotFoundException
import br.com.domain.usecase.DoLoginUseCase
import br.com.domain.usecase.GetCurrentPersonUseCase
import br.com.domain.usecase.requestvalues.DoLoginRV
import br.com.policlinsaude.R
import br.com.policlinsaude.core.helper.IntentHelper
import br.com.policlinsaude.core.helper.UseCaseHandler
import br.com.policlinsaude.login.navigator.LoginNavigator
import br.com.policlinsaude.login.view.LoginView
import io.reactivex.rxkotlin.subscribeBy

class LoginPresenterImpl(private val navigator: LoginNavigator,
                         private val doLoginUseCase: DoLoginUseCase,
                         private val getCurrentPersonUseCase: GetCurrentPersonUseCase,
                         private val view: LoginView) : LoginPresenter {


    override fun clickedButtonIamNotClient() {
        navigator.goToHomeWithoutLogin()
    }

    override fun checkHasToken() {
        UseCaseHandler.execute(getCurrentPersonUseCase)
                .doOnSubscribe {
                    view.showLoading()
                }
                .doOnTerminate {
                    view.hideLoading()
                }
                .subscribeBy(
                        onNext = {
                            navigator.goToHome()
                        },
                        onError = {
                            if (it is RealmNotFoundException || it is PreferenceNotFoundException) {
                                view.onPersonNotFound()
                            } else {
                                it.printStackTrace()
                                view.showDialogError(it)
                            }
                        }
                )
    }

    override fun clickedButtonEnter(firebaseToken: String) {
        val doLoginRV = DoLoginRV(register = view.getRegister(), order = view.getOrder(), password = view.getPassword(), firebaseToken = firebaseToken)
        UseCaseHandler.execute(doLoginUseCase, doLoginRV)
                .doOnSubscribe {
                    view.showLoginLoading()
                    view.hideButtonEnter()
                }
                .doOnTerminate {
                    view.hideLoginLoading()
                    view.showButtonEnter()
                }
                .subscribeBy(
                        onComplete = {
                            navigator.goToHome()
                        },
                        onError = {
                            view.showDialogError(it)
                        }
                )
    }

    override fun clickedButtonForgotPassword() {
        navigator.goToForgotPassword()
    }

    override fun clickedButtonNotHasPassword() {
        navigator.goToNotHasPassword()
    }

    override fun clickedEye() {
       view.changeEye()
    }

    override fun clickedLink() {
        view.clickedLink()
    }

}