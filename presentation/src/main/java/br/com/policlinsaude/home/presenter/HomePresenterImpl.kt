package br.com.policlinsaude.home.presenter

import br.com.domain.usecase.GetBannersUseCase
import br.com.domain.usecase.GetCurrentPersonUseCase
import br.com.policlinsaude.core.helper.UseCaseHandler
import br.com.policlinsaude.home.view.HomeView
import io.reactivex.rxkotlin.subscribeBy

/**
 * Created by lmiyagi on 3/23/18.
 */
class HomePresenterImpl(private val view: HomeView,
                        private val getCurrentPersonUseCase: GetCurrentPersonUseCase,
                        private val getBannersUseCase: GetBannersUseCase) : HomePresenter {

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
}