package br.com.policlinsaude.login.navigator

interface LoginNavigator {
    fun goToHome()
    fun goToNotHasPassword()
    fun goToForgotPassword()
    fun goToHomeWithoutLogin()
}

