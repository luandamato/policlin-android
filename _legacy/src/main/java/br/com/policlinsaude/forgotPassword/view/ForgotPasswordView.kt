package br.com.policlinsaude.forgotPassword.view

interface ForgotPasswordView {
    fun showLoading()
    fun hideLoading()
    fun showButtonRecoverPassword()
    fun hideButtonRecoverPassword()
    fun getRegister(): String
    fun getEmail(): String
    fun getOrder(): String
    fun showDialogError(it: Throwable)
    fun showEmailSentDialog()
}