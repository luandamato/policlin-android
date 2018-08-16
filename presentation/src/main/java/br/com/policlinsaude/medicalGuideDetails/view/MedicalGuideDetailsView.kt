package br.com.policlinsaude.medicalGuideDetails.view

import br.com.domain.model.MedicalGuidePlan
import br.com.policlinsaude.model.PresentationEstablishment
import br.com.policlinsaude.model.PresentationPlan

interface MedicalGuideDetailsView {
    fun showMedicalGuideDetails(presentationEstablishment: PresentationEstablishment)
    fun showDialogError(it: Throwable)
    fun showPlansDialog(plans: List<MedicalGuidePlan>?)
    fun showPlansLoading()
    fun hidePlansLoading()
    fun showEmptyPlansDialog()
    fun showLoading()
    fun hideLoading()
    fun showFavoriteSuccessMessage()
    fun showLoginDialog()
    fun setFavorited(favorited: Boolean)
    fun showRemoveFavoriteSuccessMessage()
    fun showSelectPhones(phoneOne: String, phoneTwo: String)
    fun showWithoutNetworkDialog()
    fun execGetFavorites()
    fun saveFavoritesInPrefs(favorites: List<PresentationEstablishment>)
}