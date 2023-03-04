package br.com.policlinsaude.home.view.model

import androidx.annotation.DrawableRes
import br.com.policlinsaude.R

enum class PresentationHomeOptionEnum(@DrawableRes val drawable: Int) {
    MEDICAL_GUIDE(R.drawable.medical_guide),
    HEALTH_INSURANCE(R.drawable.health_insurance),
    TICKET(R.drawable.tickets),
    FAVORITES(R.drawable.favorites),
    OWN_NETWORK(R.drawable.own_network),
    FACTOR_EXTRACTOR(R.drawable.factor_extractor),
    RESEARCH_VALUES_CO_PARTICIPATION(R.drawable.research_coparticipation_values),
    INCOME_TAX(R.drawable.income_tax),
} 