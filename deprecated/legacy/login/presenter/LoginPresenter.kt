package br.com.policlinsaude.login.presenter

interface LoginPresenter {
    fun clickedButtonEnter(firebaseToken: String)
    fun clickedButtonForgotPassword()
    fun clickedButtonNotHasPassword()
    fun clickedButtonIamNotClient()
    fun checkHasToken()
    fun clickedEye()
    fun clickedLink()
}