package br.com.policlinsaude.core.helper

import android.content.Context
import android.content.Intent
import android.net.Uri


/**
 * Created by lmiyagi on 3/27/18.
 */
object IntentHelper {

    fun openUrlInBrowser(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        context.startActivity(intent)
    }
}