package br.com.policlinsaude.home.view.model

import android.support.annotation.DrawableRes
import br.com.policlinsaude.R

enum class PresentationHomeOptionEnum(@DrawableRes val drawable: Int) {
    MEDICAL_GUIDE(R.drawable.medical_guide),
    OWN_NETWORK(R.drawable.own_network),
    FAVORITES(R.drawable.favorites),
    HEALTH_INSURANCE(R.drawable.health_insurance)
}