package br.com.policlinsaude.ui.legacy.editPassword.presenter

interface EditPasswordPresenter {
    fun onSendClicked(password: String)
    fun onSuccessDialogDismissed()
}