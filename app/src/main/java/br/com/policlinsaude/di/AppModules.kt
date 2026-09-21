package br.com.policlinsaude.di

import br.com.policlinsaude.data.local.SessionManager
import br.com.policlinsaude.data.repositories.AppRepository
import br.com.policlinsaude.ui.activities.forgotPassword.ForgotPasswordViewModel
import br.com.policlinsaude.ui.activities.home.MenuViewModel
import br.com.policlinsaude.ui.activities.login.LoginViewModel
import br.com.policlinsaude.ui.activities.notHasPassword.NotHasPasswordViewModel
import br.com.policlinsaude.ui.fragments.home.HomeViewModel
import br.com.policlinsaude.ui.fragments.notifications.NotificationViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Módulo de injeção de dependência da nova arquitetura (Koin).
 *
 * Registra a camada de dados (repository + sessão) como singletons,
 * e os ViewModels das features.
 */
val appModules = module {
    single { AppRepository() }
    single { SessionManager(androidContext()) }

    viewModel { LoginViewModel(get(), get()) }
    viewModel { ForgotPasswordViewModel(get()) }
    viewModel { NotHasPasswordViewModel(get()) }
    viewModel { MenuViewModel(get(), get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { NotificationViewModel(get(), get()) }
}
