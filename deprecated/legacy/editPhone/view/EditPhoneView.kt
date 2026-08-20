package br.com.policlinsaude.ui.legacy.editPhone.view

interface EditPhoneView {
    fun closeView()
    fun showSuccessMessage()
    fun showErrorDialog(throwable: Throwable)
    fun showLoading()
    fun hideLoading()
}