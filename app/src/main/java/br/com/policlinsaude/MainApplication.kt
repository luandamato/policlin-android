package br.com.policlinsaude

import android.app.Application

/**
 * Application do app sob a nova arquitetura.
 *
 * Punto de entrada do ciclo de vida. Por agora não inicializa
 * nenhum stack de deps (Dagger/Koin) — se reexará a medida que se
 * migren features desde `_legacy` e se unifique `data`/`ui`/`util`.
 */
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}