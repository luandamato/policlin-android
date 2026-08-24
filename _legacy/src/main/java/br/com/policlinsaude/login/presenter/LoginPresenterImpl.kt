package br.com.policlinsaude.login.presenter

import android.os.Build
import br.com.policlinsaude.data.exception.PreferenceNotFoundException
import br.com.policlinsaude.domain.usecase.DoLoginUseCase
import br.com.policlinsaude.domain.usecase.GetCurrentPersonUseCase
import br.com.policlinsaude.domain.usecase.requestvalues.DoLoginRV
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
                            if (it is PreferenceNotFoundException) {
                                view.onPersonNotFound()
                            } else {
                                it.printStackTrace()
                                view.showDialogError(it)
                            }
                        }
                )
    }

    override fun clickedButtonEnter(firebaseToken: String) {
        val version = Build.VERSION.RELEASE
        val doLoginRV = DoLoginRV(register = view.getRegister(), order = view.getOrder(), password = view.getPassword(), firebaseToken = firebaseToken, osVersion = version)
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
