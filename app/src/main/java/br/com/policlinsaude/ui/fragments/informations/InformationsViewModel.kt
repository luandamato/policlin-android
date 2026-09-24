package br.com.policlinsaude.ui.fragments.informations

import androidx.lifecycle.ViewModel
import br.com.policlinsaude.data.services.NetworkConstants

class InformationsViewModel : ViewModel() {

    fun getAppVersion(): String {
        return NetworkConstants.APP_VERSION
    }
}
