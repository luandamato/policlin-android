package br.com.policlinsaude.ui.legacy.perfil.di

import br.com.domain.usecase.GetCurrentPersonUseCase
import br.com.domain.usecase.UpdateAvatarUseCase
import br.com.policlinsaude.perfil.navigator.PerfilNavigator
import br.com.policlinsaude.perfil.navigator.PerfilNavigatorImpl
import br.com.policlinsaude.perfil.presenter.PerfilPresenter
import br.com.policlinsaude.perfil.presenter.PerfilPresenterImpl
import br.com.policlinsaude.perfil.view.PerfilFragment
import br.com.policlinsaude.perfil.view.PerfilView
import dagger.Module
import dagger.Provides

@Module
class PerfilModule {

    @Provides
    fun providesPerfilPresenter(getCurrentPersonUseCase: GetCurrentPersonUseCase, view: PerfilView, navigator: PerfilNavigator, updateAvatarUseCase: UpdateAvatarUseCase)
            : PerfilPresenter = PerfilPresenterImpl(getCurrentPersonUseCase, view, navigator, updateAvatarUseCase)

    @Provides
    fun providesPerfilView(fragment: PerfilFragment)
            : PerfilView = fragment

    @Provides
    fun providesNavigator(fragment: PerfilFragment)
            : PerfilNavigator = PerfilNavigatorImpl(fragment)
}