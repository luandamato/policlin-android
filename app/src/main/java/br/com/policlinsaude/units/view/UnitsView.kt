package br.com.policlinsaude.units.view

import br.com.policlinsaude.model.PresentationEstablishment
import br.com.policlinsaude.model.PresentationQualification

interface UnitsView {
    fun showLoading()
    fun hideLoading()
   // fun showUnits(units: List<Pair<String, List<PresentationEstablishment>>>, qualifications: MutableList<PresentationQualification>)
   fun showUnits(units: MutableList<PresentationEstablishment>?, qualifications: MutableList<PresentationQualification>)
    fun showDialogError(it: Throwable)
}