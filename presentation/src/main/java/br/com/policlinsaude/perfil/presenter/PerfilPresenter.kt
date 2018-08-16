package br.com.policlinsaude.perfil.presenter

interface PerfilPresenter {
    fun getPerfil()
    fun onAvatarChangeClicked()
    fun onImagePicked(imageString: String?)
    fun onImagePickError()
    fun onEditPhoneClicked()
    fun onEditPasswordClicked()
}