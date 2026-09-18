package br.com.policlinsaude.ui.fragments.home

import androidx.annotation.DrawableRes
import br.com.policlinsaude.R

/**
 * Opciones (tiles) de la pantalla home.
 * Migrado de `_legacy/.../home/view/model/PresentationHomeOptionEnum.kt`.
 */
enum class HomeOptionEnum(@DrawableRes val drawable: Int) {

    SCHEDULE(R.drawable.central_de_atendimento),
    MEDICAL_GUIDE(R.drawable.medical_guide),
    HEALTH_INSURANCE(R.drawable.health_insurance),
    TICKET(R.drawable.tickets),
    FAVORITES(R.drawable.favorites),
    OWN_NETWORK(R.drawable.own_network),
    FACTOR_EXTRACTOR(R.drawable.factor_extractor),
    RESEARCH_VALUES_CO_PARTICIPATION(R.drawable.research_coparticipation_values),
    INCOME_TAX(R.drawable.income_tax),
    GUIDE_AUTHORIZER(R.drawable.autorizador_guia),
    SERVICE_TOKEN(R.drawable.gerar_token),
}