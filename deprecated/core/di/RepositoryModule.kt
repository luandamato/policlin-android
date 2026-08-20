package br.com.policlinsaude.core.di

import br.com.policlinsaude.data.repository.AuthRepositoryImpl
import br.com.policlinsaude.data.repository.MedicalGuideRepositoryImpl
import br.com.policlinsaude.data.repository.UserRepository
import br.com.policlinsaude.data.repository.UserRepositoryImpl
import br.com.policlinsaude.domain.repository.AuthRepository
import br.com.policlinsaude.domain.repository.MedicalGuideRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindMedicalGuideRepository(
        impl: MedicalGuideRepositoryImpl
    ): MedicalGuideRepository
}
