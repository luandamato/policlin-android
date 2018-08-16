package br.com.policlinsaude.editPhone.presenter

interface EditPhonePresenter {
    fun onSendClicked(phone: String)
    fun onSuccessDialogDismissed()
}