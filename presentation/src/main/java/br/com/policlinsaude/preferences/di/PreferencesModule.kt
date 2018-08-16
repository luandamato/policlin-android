package br.com.policlinsaude.preferences.di

import br.com.domain.repository.Repository
import br.com.domain.usecase.GetBooleanPreferenceUseCase
import br.com.domain.usecase.SetLocationPreferenceUseCase
import br.com.domain.usecase.SetNotificationPreferenceUseCase
import br.com.domain.usecase.DoLogoffUseCase
import br.com.policlinsaude.preferences.navigator.PreferencesNavigator
import br.com.policlinsaude.preferences.navigator.PreferencesNavigatorImpl
import br.com.policlinsaude.preferences.presenter.PreferencesPresenter
import br.com.policlinsaude.preferences.presenter.PreferencesPresenterImpl
import br.com.policlinsaude.preferences.view.PreferencesFragment
import br.com.policlinsaude.preferences.view.PreferencesView
import dagger.Module
import dagger.Provides

/**
 * Created by lmiyagi on 3/26/18.
 */
@Module
class PreferencesModule {

    @Provides
    fun providePreferencesView(preferencesFragment: PreferencesFragment)
            : PreferencesView = preferencesFragment

    @Provides
    fun providePreferencesPresenter(view: PreferencesView,
                                    navigator: PreferencesNavigator,
                                    getBooleanPreferenceUseCase: GetBooleanPreferenceUseCase,
                                    setNotificationPreferenceUseCase: SetNotificationPreferenceUseCase,
                                    setLocationPreferenceUseCase: SetLocationPreferenceUseCase,
                                    doLogoffUseCase: DoLogoffUseCase)
            : PreferencesPresenter = PreferencesPresenterImpl(view,
            navigator,
            getBooleanPreferenceUseCase,
            setNotificationPreferenceUseCase,
            setLocationPreferenceUseCase,
            doLogoffUseCase)

    @Provides
    fun provideDoLogoff(repository: Repository)
            : DoLogoffUseCase = DoLogoffUseCase(repository)

    @Provides
    fun provideNavigator(preferencesFragment: PreferencesFragment)
            : PreferencesNavigator = PreferencesNavigatorImpl(preferencesFragment)
}