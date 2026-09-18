package br.com.policlinsaude.data.services

/**
 * Constantes de rede consolidadas do app.
 *
 * Reúne as bases URL usadas pelos stacks antigos
 * (ver `_legacy/.../NetworkingModule` e `_legacy/.../otherFeatures/data/networking/RetrofitInstance.kt`).
 */
object NetworkConstants {

    /**
     * Versión de la app enviada en el header `versao` de cada request a API.
     *
     * ¡Mantener sincronizada con el `versionName` de `app/build.gradle`!
     * (Antes se leía de `androidx.multidex.BuildConfig.VERSION_NAME`, que es un
     * stub con valor vacío — por eso el header iba siempre vacío.)
     */
    const val APP_VERSION = "1.60.10"

    /** Base principal (stack novo / outros endpoints). */
    const val BASE_URL = "http://policlinsaude.com.br/mapp/api/"

    /** Base de notificações / perfil (stack de notificações). */
    const val BASE_URL_APIAPP = "http://policlinsaude.com.br/apiapp/"

    // Códigos de resposta úteis
    const val CODE_SUCCESS = 200
    const val CODE_RESPONSE_SUCCESS = 1
    const val CODE_RESPONSE_UNAUTHORIZED = 401
    const val CODE_WITHOUT_NETWORK = 0
}