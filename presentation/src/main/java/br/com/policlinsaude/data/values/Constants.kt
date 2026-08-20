package br.com.policlinsaude.data.values

/**
 * Constantes e valores globais da aplicação
 */

object Constants {
    // API
    const val BASE_URL = "https://api.policlinsaude.com.br/"
    const val TIMEOUT = 30L // segundos

    // SharedPreferences
    const val PREF_NAME = "policlin_preferences"
    const val PREF_TOKEN = "auth_token"
    const val PREF_USER_ID = "user_id"
    const val PREF_REFRESH_TOKEN = "refresh_token"

    // HTTP Headers
    const val HEADER_AUTHORIZATION = "Authorization"
    const val HEADER_CONTENT_TYPE = "Content-Type"
    const val CONTENT_TYPE_JSON = "application/json"

    // Tamanhos padrão
    const val PAGE_SIZE = 20
    const val GRID_COLUMNS = 2

    // URLs
    const val TERMS_URL = "https://www.policlinsaude.com.br/terms"
    const val PRIVACY_URL = "https://www.policlinsaude.com.br/privacy"
}

object StatusCode {
    const val SUCCESS = 200
    const val CREATED = 201
    const val BAD_REQUEST = 400
    const val UNAUTHORIZED = 401
    const val FORBIDDEN = 403
    const val NOT_FOUND = 404
    const val INTERNAL_ERROR = 500
}

object ErrorMessages {
    const val NETWORK_ERROR = "Erro de conexão. Tente novamente."
    const val INVALID_CREDENTIALS = "Matrícula, Ordem ou Senha incorretos."
    const val TOKEN_EXPIRED = "Sua sessão expirou. Faça login novamente."
    const val GENERIC_ERROR = "Algo deu errado. Tente novamente."
    const val EMPTY_FIELDS = "Preencha todos os campos."
}

enum class LoginStatus {
    IDLE,
    LOADING,
    SUCCESS,
    ERROR,
    TOKEN_CHECKING
}

enum class AuthorizationStatus {
    PENDING,
    APPROVED,
    DENIED,
    EXPIRED
}
