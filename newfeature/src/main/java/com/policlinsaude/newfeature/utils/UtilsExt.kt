package com.policlinsaude.newfeature.utils

import android.provider.ContactsContract.CommonDataKinds.StructuredPostal
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.policlinsaude.newfeature.data.networking.ServerErrorResponse
import kotlinx.coroutines.coroutineScope
import retrofit2.Response
import java.lang.reflect.Method
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY
import java.text.NumberFormat
import java.util.*


suspend fun <T> makeRequest(call: suspend () -> Response<T>): T {
    return coroutineScope {
        val response = try {
            call.invoke()
        } catch (ex: Exception) {
            throw ServerErrorResponse.verifyError(ex)
        }

        if(!response.isSuccessful) {
            throw ServerErrorResponse.verifyError(response.errorBody(), response.code())
        }

        return@coroutineScope response.body() as T
    }
}


suspend fun Method.invokeSuspend(obj: Any, vararg args: Any?): Any? =
    suspendCoroutine { cont ->
        val ret=invoke(obj, *args, cont)
        cont.resume(ret)
    }


fun String.toCurrencyBRL(): String {
    val format = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    return format.format(this.toDouble())
}