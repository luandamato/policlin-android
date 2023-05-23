package br.com.policlinsaude.home.presenter

import android.os.UserHandle
import br.com.domain.model.UserConnected
import br.com.domain.model.ValidateButtons
import br.com.domain.usecase.*
import br.com.domain.usecase.requestvalues.GetValidationUserConnectedRV
import br.com.domain.usecase.requestvalues.RecoverPasswordRV
import br.com.policlinsaude.core.helper.UseCaseHandler
import br.com.policlinsaude.home.view.HomeView
import br.com.policlinsaude.preferences.navigator.PreferencesNavigator
import io.reactivex.rxkotlin.subscribeBy

/**
 * Created by lmiyagi on 3/23/18.
 */
class HomePresenterImpl(private val view: HomeView,
                        private val getCurrentPersonUseCase: GetCurrentPersonUseCase,
                        private val getBannersUseCase: GetBannersUseCase,
                        private val getValidationUserConnected: GetValidationUserConnectedUseCase,
                        private val doLogoffUseCase: DoLogoffUseCase,
                        private val validateButtons: GetValidationButtonUseCase
) : HomePresenter {

    override fun onViewAttached() {
        getCurrentPerson()
        getBanners()
    }

    private fun getBanners() {
        UseCaseHandler.execute(getBannersUseCase)
                .doOnSubscribe {
                    view.showBannerLoading()
                }
                .doOnTerminate {
                    view.hideBannerLoading()
                }
                .subscribeBy(
                        onNext = {
                            if (it.isEmpty()) {
                                view.renderEmptyBanners()
                            } else {
                                view.renderBanners(it)
                            }

                        },
                        onError = {
                            it.printStackTrace()
                        }
                )
    }

    private fun getCurrentPerson() {
        UseCaseHandler.execute(getCurrentPersonUseCase)
                .subscribeBy(
                        onNext = {
                            view.renderPerson(it)
                        },
                        onError = {
                            it.printStackTrace()
                        })
    }

    override fun onMenuClickedAsGuest() {
        view.showLoginDialog()
    }

    override fun onValidateConnectedUser(registration: String, order: String) {
        val data = GetValidationUserConnectedRV(registration = registration, order = order)
        UseCaseHandler.execute(getValidationUserConnected,data)
            .subscribeBy(
                onNext = {
                    if(it.codAcao == 450)
                        view.showUpdateDialog(Throwable(it.msgExterna.orEmpty()))

                    else if(it.codAcao == 5)
                        onLogout(it)
                }, onError = {
                    it.printStackTrace()
                }
            )
    }

    override fun onValidateButtons() {
        UseCaseHandler.execute(validateButtons)
            .subscribeBy(
                onNext = {
                    view.showButtons(it)
                }, onError = {
                    view.showButtons(ValidateButtons())
                    it.printStackTrace()
                }
            )
    }

    private fun onLogout(data: UserConnected) {
        UseCaseHandler.execute(doLogoffUseCase)
            .subscribeBy(
                onComplete = {
                    view.showUserNotConnectedDialog(data)
                },
                onError = {
                    it.printStackTrace()
                }
            )
    }
}