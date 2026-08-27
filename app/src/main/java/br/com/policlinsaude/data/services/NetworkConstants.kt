package br.com.policlinsaude.data.services

/**
 * Constantes de rede consolidadas do app.
 *
 * Reúne as bases URL usadas pelos stacks antigos
 * (ver `_legacy/.../NetworkingModule` e `_legacy/.../otherFeatures/data/networking/RetrofitInstance.kt`).
 */
object NetworkConstants {

    /** Base principal (stack novo / outros endpoints). */
    const val BASE_URL = "http://policlinsaude.com.br/apiapp/"

    /** Base de notificações / perfil (stack de notificações). */
    const val BASE_URL_NOTIFICATION = "http://policlinsaude.com.br/mapp/api/"

    // Códigos de resposta úteis
    const val CODE_SUCCESS = 200
    const val CODE_RESPONSE_SUCCESS = 1
    const val CODE_RESPONSE_UNAUTHORIZED = 401
    const val CODE_WITHOUT_NETWORK = 0
}