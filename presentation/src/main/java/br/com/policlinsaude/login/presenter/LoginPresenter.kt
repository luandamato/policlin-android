package br.com.policlinsaude.login.presenter

interface LoginPresenter {
    fun clickedButtonEnter()
    fun clickedButtonForgotPassword()
    fun clickedButtonNotHasPassword()
    fun clickedButtonIamNotClient()
    fun checkHasToken()
    fun clickedEye()
    fun clickedLink()
}