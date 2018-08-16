package br.com.policlinsaude.home.presenter

import br.com.data.exception.PreferenceNotFoundException
import br.com.data.exception.RealmNotFoundException
import br.com.domain.usecase.GetCurrentPersonUseCase
import br.com.policlinsaude.core.helper.UseCaseHandler
import br.com.policlinsaude.home.navigator.MenuNavigator
import br.com.policlinsaude.home.view.MenuView
import io.reactivex.rxkotlin.subscribeBy

/**
 * Created by lmiyagi on 3/19/18.
 */
class MenuPresenterImpl(private val navigator: MenuNavigator,
                        private val getCurrentPersonUseCase: GetCurrentPersonUseCase,
                        private val view: MenuView) : MenuPresenter {

    override fun getCurrentPerson() {
        UseCaseHandler.execute(getCurrentPersonUseCase)
                .subscribeBy(
                        onNext = {
                            view.renderPerson(it)
                        },
                        onError = {
                            it.printStackTrace()
                            if (it is PreferenceNotFoundException ||
                                    it is RealmNotFoundException) {
                                view.setupGuest()
                            } else {
                                view.showError(it)
                            }
                        }
                )
    }

    override fun onMenuClickedAsGuest() {
        view.showLoginDialog()
    }

    override fun onLoginClicked() {
        navigator.goToLogin()
    }
}