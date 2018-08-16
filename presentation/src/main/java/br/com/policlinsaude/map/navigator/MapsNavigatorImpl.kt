package br.com.policlinsaude.map.navigator

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings

/**
 * Created by lmiyagi on 05/04/18.
 */
class MapsNavigatorImpl(private val activity: Activity) : MapsNavigator {

    override fun goToSettings() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.data = Uri.parse("package:" + activity.packageName)
        activity.startActivity(intent)
    }

    override fun goToLocationSettings() {
        activity.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
    }
}