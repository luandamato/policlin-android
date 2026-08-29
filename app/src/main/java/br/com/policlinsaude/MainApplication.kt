package br.com.policlinsaude

import android.app.Application
import br.com.policlinsaude.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * Application do app sob a nova arquitetura.
 *
 * Inicializa o Koin (DI) com a camada de dados e, a medida que as
 * features forem migradas, seus ViewModels.
 */
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApplication)
            modules(appModules)
        }
    }
}