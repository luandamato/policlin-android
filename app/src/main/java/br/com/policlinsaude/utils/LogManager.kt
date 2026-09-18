package br.com.policlinsaude.utils

import android.util.Log

/**
 * Gerenciador centralizado de logs da aplicação.
 *
 * Uso:
 * - Em desenvolvimento (BuildConfig.DEBUG=true): Todos os logs são impressos
 * - Em produção (BuildConfig.DEBUG=false): Apenas erros (Log.e) são impressos
 *
 * Exemplos:
 * ```
 * LogManager.d("MyTag", "Mensagem de debug")  // Só aparece em DEBUG
 * LogManager.e("MyTag", "Erro crítico")       // Sempre aparece
 * LogManager.i("MyTag", "Informação")         // Só aparece em DEBUG
 * LogManager.w("MyTag", "Aviso")              // Só aparece em DEBUG
 * ```
 */
object LogManager {

    /**
     * Log de DEBUG (aparece apenas em desenvolvimento)
     */
    fun d(tag: String, message: String, throwable: Throwable? = null) {
        if (isDebug()) {
            if (throwable != null) {
                Log.d(tag, message, throwable)
            } else {
                Log.d(tag, message)
            }
        }
    }

    /**
     * Log de ERRO (sempre aparece, mesmo em produção)
     */
    fun e(tag: String, message: String, throwable: Throwable? = null) {
        if (throwable != null) {
            Log.e(tag, message, throwable)
        } else {
            Log.e(tag, message)
        }
    }

    /**
     * Log de INFO (aparece apenas em desenvolvimento)
     */
    fun i(tag: String, message: String, throwable: Throwable? = null) {
        if (isDebug()) {
            if (throwable != null) {
                Log.i(tag, message, throwable)
            } else {
                Log.i(tag, message)
            }
        }
    }

    /**
     * Log de AVISO (aparece apenas em desenvolvimento)
     */
    fun w(tag: String, message: String, throwable: Throwable? = null) {
        if (isDebug()) {
            if (throwable != null) {
                Log.w(tag, message, throwable)
            } else {
                Log.w(tag, message)
            }
        }
    }

    /**
     * Log de VERBOSE (aparece apenas em desenvolvimento)
     */
    fun v(tag: String, message: String, throwable: Throwable? = null) {
        if (isDebug()) {
            if (throwable != null) {
                Log.v(tag, message, throwable)
            } else {
                Log.v(tag, message)
            }
        }
    }

    /**
     * Imprime uma seção separada de logs (útil para debug visual)
     */
    fun section(tag: String, title: String) {
        if (isDebug()) {
            val separator = "=".repeat(50)
            Log.d(tag, separator)
            Log.d(tag, title)
            Log.d(tag, separator)
        }
    }

    /**
     * Imprime um objeto ou estrutura de dados (útil para debug)
     */
    fun data(tag: String, label: String, data: Any?) {
        if (isDebug()) {
            Log.d(tag, "$label: ${data?.toString() ?: "null"}")
        }
    }

    /**
     * Verifica se está em modo debug
     * Usa reflexão para acessar BuildConfig gerado automaticamente pelo Gradle
     */
    private fun isDebug(): Boolean {
        return try {
            val buildConfigClass = Class.forName("br.com.policlinsaude.BuildConfig")
            val debugField = buildConfigClass.getField("DEBUG")
            debugField.getBoolean(null)
        } catch (e: Exception) {
            // Fallback: assume DEBUG em caso de erro
            true
        }
    }
}
