package br.com.policlinsaude.ui.legacy.notHasPassword.navigator

import br.com.policlinsaude.notHasPassword.view.NotHasPasswordActivity

class NotHasPasswordNavigatorImpl(private val activity: NotHasPasswordActivity): NotHasPasswordNavigator {
    override fun finishScreen() {
        activity.finish()
    }
}