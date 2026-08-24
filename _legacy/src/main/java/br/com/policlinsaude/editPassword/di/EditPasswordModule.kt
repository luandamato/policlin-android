package br.com.policlinsaude.editPassword.di

import br.com.policlinsaude.domain.repository.Repository
import br.com.policlinsaude.domain.usecase.EditPasswordUseCase
import br.com.policlinsaude.editPassword.presenter.EditPasswordPresenter
import br.com.policlinsaude.editPassword.presenter.EditPasswordPresenterImpl
import br.com.policlinsaude.editPassword.view.EditPasswordActivity
import br.com.policlinsaude.editPassword.view.EditPasswordView
import dagger.Module
import dagger.Provides

@Module
class EditPasswordModule {

    @Provides
    fun providePresenter(view: EditPasswordView,
                         editPasswordUseCase: EditPasswordUseCase)
            : EditPasswordPresenter = EditPasswordPresenterImpl(view, editPasswordUseCase)

    @Provides
    fun provideView(activity: EditPasswordActivity)
            : EditPasswordView = activity

    @Provides
    fun provideEditPasswordUseCase(repository: Repository)
            : EditPasswordUseCase = EditPasswordUseCase(repository)
}