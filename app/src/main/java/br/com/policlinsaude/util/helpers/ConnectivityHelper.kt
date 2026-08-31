package br.com.policlinsaude.util.helpers

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

/**
 * Verificação de conectividade com a internet.
 * Migrado/adaptado de `_legacy/.../data/datasource/networking/CheckInternetConnection.kt`.
 */
object ConnectivityHelper {

    fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            ?: return false

        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ||
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
}