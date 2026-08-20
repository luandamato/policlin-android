package br.com.policlinsaude.ui.legacy.home.di

import br.com.domain.repository.Repository
import br.com.domain.usecase.DoLogoffUseCase
import br.com.domain.usecase.GetCurrentPersonUseCase
import br.com.policlinsaude.home.navigator.MenuNavigator
import br.com.policlinsaude.home.navigator.MenuNavigatorImpl
import br.com.policlinsaude.home.presenter.MenuPresenter
import br.com.policlinsaude.home.presenter.MenuPresenterImpl
import br.com.policlinsaude.home.view.MenuActivity
import br.com.policlinsaude.home.view.MenuView
import dagger.Module
import dagger.Provides

@Module
class MenuModule {

    @Provides
    fun provideMenuView(activity: MenuActivity)
            : MenuView = activity

    @Provides
    fun provideMenuPresenter(navigator: MenuNavigator,
                             getCurrentPersonUseCase: GetCurrentPersonUseCase,
                             doLogoffUseCase: DoLogoffUseCase,
                             view: MenuView)
            : MenuPresenter = MenuPresenterImpl(navigator, getCurrentPersonUseCase,doLogoffUseCase, view)


    @Provides
    fun provideNavigator(menuActivity: MenuActivity)
            : MenuNavigator = MenuNavigatorImpl(menuActivity, menuActivity.supportFragmentManager)
    @Provides
    fun provideDoLogoff(repository: Repository)
            : DoLogoffUseCase = DoLogoffUseCase(repository)
}