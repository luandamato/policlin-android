package br.com.policlinsaude.editPassword.presenter

interface EditPasswordPresenter {
    fun onSendClicked(password: String)
    fun onSuccessDialogDismissed()
}