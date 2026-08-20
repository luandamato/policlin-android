package br.com.policlinsaude.ui.legacy.ownNetwork.di

import br.com.domain.repository.Repository
import br.com.domain.usecase.GetOwnNetworkUseCase
import br.com.policlinsaude.ownNetwork.navigator.OwnNetworkNavigator
import br.com.policlinsaude.ownNetwork.navigator.OwnNetworkNavigatorImpl
import br.com.policlinsaude.ownNetwork.presenter.OwnNetworkPresenter
import br.com.policlinsaude.ownNetwork.presenter.OwnNetworkPresenterImpl
import br.com.policlinsaude.ownNetwork.view.OwnNetworkActivity
import br.com.policlinsaude.ownNetwork.view.OwnNetworkView
import br.com.policlinsaude.ownNetwork.view.adapter.OwnNetworkPageAdapter
import dagger.Module
import dagger.Provides

@Module
class OwnNetworkModule {

    @Provides
    fun providesMedicalGuideListPresenter(getOwnNetworkUseCase: GetOwnNetworkUseCase,
                                          view: OwnNetworkActivity,
                                          navigator: OwnNetworkNavigator)
            : OwnNetworkPresenter = OwnNetworkPresenterImpl(
            getOwnNetworkUseCase = getOwnNetworkUseCase, view = view, navigator = navigator)

    @Provides
    fun provideOwnNetworkView(activity: OwnNetworkActivity)
            : OwnNetworkView = activity

    @Provides
    fun provideGetOwnNetworkUseCase(repository: Repository)
            : GetOwnNetworkUseCase = GetOwnNetworkUseCase(repository = repository)

    @Provides
    fun provideOwnNetworkPageAdapter(activity: OwnNetworkActivity): OwnNetworkPageAdapter
            = OwnNetworkPageAdapter(activity.supportFragmentManager)

    @Provides
    fun provideOwnNetworkNavigator(activity: OwnNetworkActivity)
            : OwnNetworkNavigator = OwnNetworkNavigatorImpl(activity)
}