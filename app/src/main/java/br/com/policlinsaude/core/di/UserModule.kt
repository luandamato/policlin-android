package br.com.policlinsaude.core.di

import br.com.policlinsaude.domain.repository.Repository
import br.com.policlinsaude.domain.usecase.GetCurrentPersonUseCase
import br.com.policlinsaude.domain.usecase.GetTokenUseCase
import br.com.policlinsaude.domain.usecase.UpdateAvatarUseCase
import dagger.Module
import dagger.Provides

/**
 * Created by lmiyagi on 3/19/18.
 */
@Module
class UserModule {

    @Provides
    fun provideGetCurrentPerson(repository: Repository)
            : GetCurrentPersonUseCase = GetCurrentPersonUseCase(repository = repository)

    @Provides
    fun provideGetTokenUseCase(repository: Repository)
            : GetTokenUseCase = GetTokenUseCase(repository = repository)

    @Provides
    fun provideUpdateAvatarUseCase(repository: Repository)
            : UpdateAvatarUseCase = UpdateAvatarUseCase(repository)
}