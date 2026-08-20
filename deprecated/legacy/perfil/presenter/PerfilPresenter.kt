package br.com.policlinsaude.ui.legacy.perfil.presenter

interface PerfilPresenter {
    fun getPerfil()
    fun onAvatarChangeClicked()
    fun onImagePicked(imageString: String?)
    fun onImagePickError()
    fun onEditPhoneClicked()
    fun onEditPasswordClicked()
    fun onDeleteClicked()
}