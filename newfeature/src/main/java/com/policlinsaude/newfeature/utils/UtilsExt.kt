package com.policlinsaude.newfeature.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.coroutineScope
import retrofit2.Response
import java.lang.reflect.Method
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun <T> makeRequest(call: suspend () -> Response<T>): T {
    return coroutineScope {
        val response = try {
            call.invoke()
        } catch (ex: Exception) {
            throw Exception() //ServerErrorResponse.verifyError(ex)
        }

        if(!response.isSuccessful) {
            throw Exception()//ServerErrorResponse.verifyError(response.errorBody(), response.code())
        }

        return@coroutineScope response.body() as T
    }
}


suspend fun Method.invokeSuspend(obj: Any, vararg args: Any?): Any? =
    suspendCoroutine { cont ->
        val ret=invoke(obj, *args, cont)
        cont.resume(ret)
    }

inline fun <reified T> Gson.fromJson(json: String) = fromJson<T>(json, object: TypeToken<T>() {}.type)