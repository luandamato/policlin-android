package br.com.policlinsaude.home.di

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
                             view: MenuView)
            : MenuPresenter = MenuPresenterImpl(navigator, getCurrentPersonUseCase, view)


    @Provides
    fun provideNavigator(menuActivity: MenuActivity)
            : MenuNavigator = MenuNavigatorImpl(menuActivity, menuActivity.supportFragmentManager)
}