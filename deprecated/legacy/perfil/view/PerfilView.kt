package br.com.policlinsaude.ui.legacy.perfil.view

import br.com.policlinsaude.model.PresentationPerson

interface PerfilView {

    fun set(person: PresentationPerson)
    fun showImagePickError()
    fun showLoading()
    fun hideLoading()
    fun showAvatarChangedSuccess()
    fun showDialogError(throwable: Throwable)

}