package br.com.policlinsaude.editPhone.di

import br.com.domain.repository.Repository
import br.com.domain.usecase.UpdateEmailUseCase
import br.com.policlinsaude.editPhone.presenter.EditPhonePresenter
import br.com.policlinsaude.editPhone.presenter.EditPhonePresenterImpl
import br.com.policlinsaude.editPhone.view.EditPhoneActivity
import br.com.policlinsaude.editPhone.view.EditPhoneView
import dagger.Module
import dagger.Provides

@Module
class EditPhoneModule {

    @Provides
    fun provideView(activity: EditPhoneActivity)
            : EditPhoneView = activity

    @Provides
    fun providePresenter(view: EditPhoneView,
                         updateEmailUseCase: UpdateEmailUseCase)
            : EditPhonePresenter = EditPhonePresenterImpl(view, updateEmailUseCase)

    @Provides
    fun provideUpdateEmailUseCase(repository: Repository)
            : UpdateEmailUseCase = UpdateEmailUseCase(repository)
}