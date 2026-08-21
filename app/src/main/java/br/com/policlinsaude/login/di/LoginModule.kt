package br.com.policlinsaude.login.di

import br.com.policlinsaude.domain.repository.Repository
import br.com.policlinsaude.domain.usecase.DoLoginUseCase
import br.com.policlinsaude.domain.usecase.GetCurrentPersonUseCase
import br.com.policlinsaude.domain.usecase.GetTokenUseCase
import br.com.policlinsaude.login.navigator.LoginNavigator
import br.com.policlinsaude.login.navigator.LoginNavigatorImpl
import br.com.policlinsaude.login.presenter.LoginPresenter
import br.com.policlinsaude.login.presenter.LoginPresenterImpl
import br.com.policlinsaude.login.view.LoginActivity
import br.com.policlinsaude.login.view.LoginView
import dagger.Module
import dagger.Provides

@Module
class LoginModule {

    @Provides
    fun providesLoginPresenter(navigator: LoginNavigator, doLoginUseCase: DoLoginUseCase,
                               getCurrentPersonUseCase: GetCurrentPersonUseCase, view: LoginView)
            : LoginPresenter = LoginPresenterImpl(navigator = navigator,
            doLoginUseCase = doLoginUseCase, getCurrentPersonUseCase = getCurrentPersonUseCase, view = view)

    @Provides
    fun provideLoginNavigator(activity: LoginActivity)
            : LoginNavigator = LoginNavigatorImpl(activity)

    @Provides
    fun provideLoginView(activity: LoginActivity)
            : LoginView = activity

    @Provides
    fun provideDoLoginUseCase(repository: Repository)
            : DoLoginUseCase = DoLoginUseCase(repository = repository)
}