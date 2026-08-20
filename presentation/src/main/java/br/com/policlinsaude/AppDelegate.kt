package br.com.policlinsaude

import android.app.Application
import br.com.policlinsaude.data.services.ServiceLocator

/**
 * Application class simples sem Hilt
 * Apenas inicializa o ServiceLocator
 */
class AppDelegate : Application() {

    override fun onCreate() {
        super.onCreate()
        
        // Inicializar ServiceLocator para DI manual
        ServiceLocator.init(this)
    }
}
