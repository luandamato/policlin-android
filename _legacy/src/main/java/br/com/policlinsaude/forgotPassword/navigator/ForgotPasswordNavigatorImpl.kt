package br.com.policlinsaude.forgotPassword.navigator

import br.com.policlinsaude.forgotPassword.view.ForgotPasswordActivity

class ForgotPasswordNavigatorImpl(private val activity: ForgotPasswordActivity): ForgotPasswordNavigator {

    override fun finishScreen() {
        activity.finish()
    }

}