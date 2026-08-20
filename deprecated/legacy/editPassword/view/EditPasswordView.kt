package br.com.policlinsaude.ui.legacy.editPassword.view

interface EditPasswordView {
    fun showLoading()
    fun hideLoading()
    fun showSuccessMessage()
    fun showErrorDialog(throwable: Throwable)
    fun closeView()
}