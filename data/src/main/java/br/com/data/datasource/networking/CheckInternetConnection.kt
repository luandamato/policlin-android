package br.com.data.datasource.networking

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log


class CheckInternetConnection(){

    companion object {
        fun check(context: Context): Boolean {

            val checkConn = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = checkConn.activeNetworkInfo

            if (activeNetwork != null){

                if (activeNetwork.type == ConnectivityManager.TYPE_WIFI)
                    Log.d("INTERNET","CONEXAO VIA WIFI")
                if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE)
                    Log.d("INTERNET","CONEXAO VIA 3G/4G")

                return true
            } else{

                Log.d("INTERNET","SEM CONEXAO COM A INTERNET")
                return false
            }



        }
    }




}
