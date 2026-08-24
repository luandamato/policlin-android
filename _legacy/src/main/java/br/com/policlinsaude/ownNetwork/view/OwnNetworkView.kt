package br.com.policlinsaude.ownNetwork.view

import br.com.policlinsaude.model.PresentationEstablishment
import br.com.policlinsaude.model.PresentationQualification

interface OwnNetworkView {
    fun showLoading()
    fun hideLoading()
    fun showOwnNetworks(ownNetworks: List<Pair<String, List<PresentationEstablishment>>>, qualifications: MutableList<PresentationQualification>)
    fun showDialogError(it: Throwable)
}