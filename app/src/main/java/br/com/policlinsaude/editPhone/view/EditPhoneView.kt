package br.com.policlinsaude.editPhone.view

interface EditPhoneView {
    fun closeView()
    fun showSuccessMessage()
    fun showErrorDialog(throwable: Throwable)
    fun showLoading()
    fun hideLoading()
}