package br.com.data.helper

import android.content.Context
import io.realm.Realm

object ModuleDataHelper {

    fun configureDatabase(context: Context) {
        Realm.init(context)
    }
}