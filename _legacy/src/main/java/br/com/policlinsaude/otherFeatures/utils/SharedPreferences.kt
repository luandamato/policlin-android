package com.policlinsaude.newfeature.utils

import android.content.Context

class SharedPreferences(val context: Context) {

    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun getToken(): String {
        return preferences.getString(PREFERENCES_TOKEN, "").toString()
    }
    fun getSelecaoBeneficiarioAtiva(): Boolean {
        return preferences.getBoolean(PREFERENCES_SELECAO, false)
    }

    fun removeToken(){
        val edit = preferences.edit()
        edit.putString(PREFERENCES_TOKEN, "")
        edit.apply()
    }

    companion object {
        private const val PREFERENCES_NAME = "POLICLIN_SAUDE"
        private const val PREFERENCES_TOKEN = "token"
        private const val PREFERENCES_SELECAO = "selecaoBeneficiarioAutorizador"
    }
}