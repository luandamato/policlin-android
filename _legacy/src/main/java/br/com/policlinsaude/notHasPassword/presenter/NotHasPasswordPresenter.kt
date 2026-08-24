package br.com.policlinsaude.notHasPassword.presenter

import br.com.policlinsaude.model.PresentationPerson
import br.com.policlinsaude.model.PresentationPlan
import java.io.Serializable

interface NotHasPasswordPresenter: Serializable {

    fun clickedButtonComplete()

    fun setPresentationPerson(presentationPerson: PresentationPerson)
    fun getPresentationPlan(): PresentationPlan
    fun getPresentationPerson(): PresentationPerson
    fun checkPlan(presentationPlan: PresentationPlan, onResult: (Boolean) -> Unit)
    fun onImagePicked(image: String?)
    fun onImagePickError()
    fun onSuccessDialogDismissed()
}