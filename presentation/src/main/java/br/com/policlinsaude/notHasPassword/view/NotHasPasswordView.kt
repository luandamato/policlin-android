package br.com.policlinsaude.notHasPassword.view

interface NotHasPasswordView {

    fun showLoading()
    fun hideLoading()
    fun showDialogError(it: Throwable)
    fun showImagePickError()
    fun showPhotoNeededError()
    fun showSuccessDialog(message: String)
}