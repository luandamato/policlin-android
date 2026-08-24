package br.com.policlinsaude.preferences.navigator

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import br.com.policlinsaude.login.view.LoginActivity
import br.com.policlinsaude.preferences.view.PreferencesFragment

/**
 * Created by lmiyagi on 3/26/18.
 */
class PreferencesNavigatorImpl(private val fragment: PreferencesFragment) : PreferencesNavigator {

    override fun goToLogin() {
        LoginActivity.start(fragment)
    }

    override fun goToSettings() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.data = Uri.parse("package:" + fragment.activity!!.packageName)
        fragment.startActivity(intent)
    }
}