package com.policlinsaude.newfeature.data.networking

import com.google.gson.GsonBuilder
import java.io.InterruptedIOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ServerErrorResponse (
    var code: String = "",
    var initMessage: String = "",
    var errorType: ServerErrorType = ServerErrorType.DEFAULT
): Exception(initMessage) {

    companion object {
        private val gson = GsonBuilder().disableHtmlEscaping().create()

        fun<T> verifyError(errorType: T, responseCode: Int = 0): ServerErrorResponse {
            return when(errorType) {
                is SocketTimeoutException -> {
                    getErrorModelWithSTR("TIMEOUT", responseCode)
                }

                is UnknownHostException, is InterruptedIOException -> {
                    getErrorModelWithSTR("ERROR_WITHOUT_CONNECTION_TIMEOUT", responseCode)
                }

                is String -> {
                    getErrorModelWithSTR(ServerErrorType.DEFAULT.toString(), responseCode)
                }

                else -> getErrorModelWithSTR(ServerErrorType.DEFAULT.toString(), responseCode)
            }
        }

        private fun getErrorModelWithSTR(error: String, responseCode: Int): ServerErrorResponse {
            return ServerErrorResponse(initMessage = error, code = responseCode.toString())
        }

    }

    enum class ServerErrorType() {
        DEFAULT, UNAUTHORIZED, CONNECTION
    }

    internal class ErrorModel(
        val error: String = "",
    )
}