package br.com.policlinsaude.ui.legacy.medicalGuideDetails.presenter

import android.content.Context
import br.com.policlinsaude.model.PresentationEstablishment
import br.com.policlinsaude.model.PresentationPlan
import java.util.ArrayList

interface MedicalGuideDetailsPresenter {
    fun setEstablishment(establishment: PresentationEstablishment)
    fun onPhoneClicked()
    fun onWhatsClicked()
    fun onWhatsClicked(phone: String)
    fun onMapClicked()
    fun onShareClicked()
    fun onPlansClicked()
    fun onFavoriteClicked(context: Context)
    fun onLoginClicked()
    fun onPhoneSelected(phone: String)
    fun onWhatsSelected(phone: String)

}