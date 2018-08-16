package br.com.policlinsaude.login.view

interface LoginView {
    fun showLoginLoading()
    fun hideLoginLoading()
    fun showButtonEnter()
    fun hideButtonEnter()
    fun getRegister(): String
    fun getPassword(): String
    fun getOrder(): String
    fun showDialogError(it: Throwable)
    fun onPersonNotFound()
    fun showLoading()
    fun hideLoading()
    fun changeEye()
}