package br.com.policlinsaude.ui.legacy.medicalGuideList.view

import br.com.policlinsaude.model.PresentationMedicalGuideList

interface MedicalGuideListView {
    fun showLoading()
    fun hideLoading()
    fun showMedicalGuideList(presentationMedicalGuideList: PresentationMedicalGuideList)
    fun showDialogError(it: Throwable)
    fun renderFiltersText(plan: String, city: String, speciality: String)
    fun showEmptyEstablishments()
    fun closeView()
}