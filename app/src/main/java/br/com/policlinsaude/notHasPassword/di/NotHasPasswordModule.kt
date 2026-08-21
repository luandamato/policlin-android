package br.com.policlinsaude.notHasPassword.di

import br.com.policlinsaude.domain.repository.Repository
import br.com.policlinsaude.domain.usecase.CheckPlanUseCase
import br.com.policlinsaude.domain.usecase.RegisterPasswordUseCase
import br.com.policlinsaude.notHasPassword.navigator.NotHasPasswordNavigator
import br.com.policlinsaude.notHasPassword.navigator.NotHasPasswordNavigatorImpl
import br.com.policlinsaude.notHasPassword.presenter.NotHasPasswordPresenter
import br.com.policlinsaude.notHasPassword.presenter.NotHasPasswordPresenterImpl
import br.com.policlinsaude.notHasPassword.view.NotHasPasswordActivity
import br.com.policlinsaude.notHasPassword.view.NotHasPasswordView
import dagger.Module
import dagger.Provides

@Module
class NotHasPasswordModule {

    @Provides
    fun providesNotHasPasswordPresenter(navigator: NotHasPasswordNavigator, registerPasswordUseCase: RegisterPasswordUseCase,
                                        view: NotHasPasswordView, checkPlanUseCase: CheckPlanUseCase)
            : NotHasPasswordPresenter = NotHasPasswordPresenterImpl(navigator = navigator,
            registerPasswordUseCase = registerPasswordUseCase, view = view, checkPlanUseCase = checkPlanUseCase)

    @Provides
    fun providesNotHasPasswordNavigator(activity: NotHasPasswordActivity)
            : NotHasPasswordNavigator = NotHasPasswordNavigatorImpl(activity)

    @Provides
    fun providesNotHasPasswordView(activity: NotHasPasswordActivity)
            : NotHasPasswordView = activity

    @Provides
    fun provideRegisterPasswordUseCase(repository: Repository)
            : RegisterPasswordUseCase = RegisterPasswordUseCase(repository = repository)

    @Provides
    fun provideCheckPlanUseCase(repository: Repository)
            : CheckPlanUseCase = CheckPlanUseCase(repository)

}