package br.com.policlinsaude.notHasPassword.presenter

import br.com.policlinsaude.model.PresentationPerson
import br.com.policlinsaude.model.PresentationPlan
import com.stepstone.stepper.StepperLayout
import java.io.Serializable

interface NotHasPasswordPresenter: Serializable {

    fun clickedButtonComplete()

    fun setPresentationPerson(presentationPerson: PresentationPerson)
    fun getPresentationPlan(): PresentationPlan
    fun getPresentationPerson(): PresentationPerson
    fun checkPlan(presentationPlan: PresentationPlan, callback: StepperLayout.OnNextClickedCallback?)
    fun onImagePicked(image: String?)
    fun onImagePickError()
    fun onSuccessDialogDismissed()
}