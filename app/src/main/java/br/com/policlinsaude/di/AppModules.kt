package br.com.policlinsaude.di

import br.com.policlinsaude.data.local.SessionManager
import br.com.policlinsaude.data.repositories.AppRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Módulo de injeção de dependência da nova arquitetura (Koin).
 *
 * Registra a camada de dados (repository + sessão) como singletons,
 * disponíveis para os ViewModels das features.
 */
val appModules = module {
    single { AppRepository() }
    single { SessionManager(androidContext()) }
}
