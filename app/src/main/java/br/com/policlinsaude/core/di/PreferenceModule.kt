package br.com.policlinsaude.core.di

import br.com.domain.repository.Repository
import br.com.domain.usecase.GetBooleanPreferenceUseCase
import br.com.domain.usecase.SetLocationPreferenceUseCase
import br.com.domain.usecase.SetNotificationPreferenceUseCase
import dagger.Module
import dagger.Provides

@Module
class PreferenceModule {

    @Provides
    fun provideSetNotificationPreference(repository: Repository)
            : SetNotificationPreferenceUseCase = SetNotificationPreferenceUseCase(repository)

    @Provides
    fun provideSetLocationPreference(repository: Repository)
            : SetLocationPreferenceUseCase = SetLocationPreferenceUseCase(repository)

    @Provides
    fun provideGetBooleanPreference(repository: Repository)
            : GetBooleanPreferenceUseCase = GetBooleanPreferenceUseCase(repository)
}