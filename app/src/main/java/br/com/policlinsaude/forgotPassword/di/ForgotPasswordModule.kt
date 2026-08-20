package br.com.policlinsaude.forgotPassword.di

import br.com.domain.repository.Repository
import br.com.domain.usecase.RecoverPasswordUseCase
import br.com.policlinsaude.forgotPassword.navigator.ForgotPasswordNavigator
import br.com.policlinsaude.forgotPassword.navigator.ForgotPasswordNavigatorImpl
import br.com.policlinsaude.forgotPassword.presenter.ForgotPasswordPresenter
import br.com.policlinsaude.forgotPassword.presenter.ForgotPasswordPresenterImpl
import br.com.policlinsaude.forgotPassword.view.ForgotPasswordActivity
import br.com.policlinsaude.forgotPassword.view.ForgotPasswordView
import dagger.Module
import dagger.Provides

@Module
class ForgotPasswordModule {

    @Provides
    fun providesLoginPresenter(navigator: ForgotPasswordNavigator,
                               recoverPasswordUseCase: RecoverPasswordUseCase,
                               view: ForgotPasswordView)
            : ForgotPasswordPresenter = ForgotPasswordPresenterImpl(navigator = navigator,
            recoverPasswordUseCase = recoverPasswordUseCase, view = view)

    @Provides
    fun provideLoginNavigator(activity: ForgotPasswordActivity)
            : ForgotPasswordNavigator = ForgotPasswordNavigatorImpl(activity)

    @Provides
    fun provideLoginView(activity: ForgotPasswordActivity)
            : ForgotPasswordView = activity

    @Provides
    fun provideRecoverPasswordUseCase(repository: Repository)
            : RecoverPasswordUseCase = RecoverPasswordUseCase(repository = repository)

}