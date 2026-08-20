package br.com.policlinsaude.login.navigator

import br.com.policlinsaude.forgotPassword.view.ForgotPasswordActivity
import br.com.policlinsaude.home.view.MenuActivity
import br.com.policlinsaude.login.view.LoginActivity
import br.com.policlinsaude.notHasPassword.view.NotHasPasswordActivity

class LoginNavigatorImpl(private val activity: LoginActivity): LoginNavigator {

    override fun goToHome() {
        MenuActivity.start(activity)
        activity.finish()
    }

    override fun goToNotHasPassword() {
        NotHasPasswordActivity.start(activity)
    }

    override fun goToForgotPassword() {
        ForgotPasswordActivity.start(activity)
    }

    override fun goToHomeWithoutLogin() {
        MenuActivity.start(activity)

    }
}