package br.com.policlinsaude.medicalGuideDetails.presenter

import android.content.Context
import br.com.policlinsaude.model.PresentationEstablishment
import br.com.policlinsaude.model.PresentationPlan
import java.util.ArrayList

interface MedicalGuideDetailsPresenter {
    fun setEstablishment(establishment: PresentationEstablishment)
    fun onPhoneClicked()
    fun onMapClicked()
    fun onShareClicked()
    fun onPlansClicked()
    fun onFavoriteClicked(context: Context)
    fun onLoginClicked()
    fun onPhoneSelected(phone: String)

}