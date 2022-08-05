package br.com.policlinsaude.home.di

import br.com.domain.repository.Repository
import br.com.domain.usecase.*
import br.com.policlinsaude.home.navigator.HomeNavigator
import br.com.policlinsaude.home.navigator.HomeNavigatorImpl
import br.com.policlinsaude.home.presenter.HomePresenter
import br.com.policlinsaude.home.presenter.HomePresenterImpl
import br.com.policlinsaude.home.view.HomeFragment
import br.com.policlinsaude.home.view.HomeView
import br.com.policlinsaude.home.view.adapter.HomeAdapter
import br.com.policlinsaude.home.view.adapter.HomePageAdapter
import br.com.policlinsaude.preferences.navigator.PreferencesNavigator
import br.com.policlinsaude.preferences.navigator.PreferencesNavigatorImpl
import br.com.policlinsaude.preferences.presenter.PreferencesPresenter
import br.com.policlinsaude.preferences.presenter.PreferencesPresenterImpl
import br.com.policlinsaude.preferences.view.PreferencesFragment
import br.com.policlinsaude.preferences.view.PreferencesView
import dagger.Module
import dagger.Provides

@Module
class HomeModule {

    @Provides
    fun provideHomeView(homeFragment: HomeFragment)
            : HomeView = homeFragment

    @Provides
    fun providePresenter(
        view: HomeView,
        getCurrentPersonUseCase: GetCurrentPersonUseCase,
        getBannersUseCase: GetBannersUseCase,
        getUserConnected: GetValidationUserConnectedUseCase,
        doLogoffUseCase: DoLogoffUseCase
    ): HomePresenter = HomePresenterImpl(view, getCurrentPersonUseCase, getBannersUseCase, getUserConnected, doLogoffUseCase)

    @Provides
    fun provideHomeAdapter(homeFragment: HomeFragment): HomeAdapter = HomeAdapter(homeFragment)

    @Provides
    fun provideHomePageAdapter(homeFragment: HomeFragment): HomePageAdapter = HomePageAdapter(homeFragment.context!!)

    @Provides
    fun provideHomeNavigator(homeFragment: HomeFragment): HomeNavigator = HomeNavigatorImpl(homeFragment)

    @Provides
    fun provideGetBannersUseCase(repository: Repository)
            : GetBannersUseCase = GetBannersUseCase(repository)

    @Provides
    fun provideGetValidationUserConnectedUseCase(repository: Repository)
        :GetValidationUserConnectedUseCase = GetValidationUserConnectedUseCase(repository)

    @Provides
    fun provideDoLogoff(repository: Repository)
            : DoLogoffUseCase = DoLogoffUseCase(repository)




}