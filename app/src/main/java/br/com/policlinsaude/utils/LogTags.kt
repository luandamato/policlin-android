package br.com.policlinsaude.utils

/**
 * Constantes de Tags para Logcat
 *
 * Use essas tags ao chamar LogManager para facilitar filtros no Logcat
 *
 * Filtro recomendado no Android Studio:
 * └─ tag:POLCLIN  (mostra todos os logs do app)
 *
 * Exemplos:
 * ```
 * LogManager.d(LogTags.NETWORK, "Conectando...")
 * LogManager.d(LogTags.REPOSITORY, "Buscando dados...")
 * LogManager.e(LogTags.ERROR, "Erro crítico", exception)
 * ```
 */
object LogTags {

    // Tags gerais
    const val POLCLIN = "PolClin"           // Tag padrão para tudo
    const val APP = "PolClinApp"            // Geral da app
    const val ERROR = "PolClinError"        // Erros

    // Features
    const val NETWORK = "PolClinNetwork"    // Requisições HTTP/Retrofit
    const val REPOSITORY = "PolClinRepo"    // Operações de dados
    const val VIEWMODEL = "PolClinVM"       // ViewModels
    const val FRAGMENT = "PolClinFragment"  // Fragments
    const val ACTIVITY = "PolClinActivity"  // Activities

    // Features Específicas
    const val LOGIN = "PolClinLogin"        // Login
    const val GUIDE = "PolClinGuide"        // Guia Médica
    const val AUTH = "PolClinAuth"          // Autorizador
    const val NOTIFICATION = "PolClinNotif" // Notificações
    const val PROFILE = "PolClinProfile"    // Perfil
    const val FAVORITES = "PolClinFav"      // Favoritos
    const val TICKETS = "PolClinTickets"    // Tickets

    // Database
    const val DATABASE = "PolClinDB"        // Database operations
    const val CACHE = "PolClinCache"        // Cache

    // UI
    const val UI = "PolClinUI"              // UI updates
    const val NAVIGATION = "PolClinNav"     // Navigation

    // Debug
    const val DEBUG = "PolClinDebug"        // Debug específico
}
