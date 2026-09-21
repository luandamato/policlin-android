package br.com.policlinsaude.ui.activities.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import br.com.policlinsaude.R

/**
 * Ítems del menú lateral (drawer).
 * Migrado de `_legacy/.../home/view/model/PresentationMenuEnum.kt`.
 */
enum class MenuOptionEnum(@StringRes val title: Int, @DrawableRes val icon: Int) {

    HOME(R.string.title_home, R.drawable.ic_home_big),
    PROFILE(R.string.title_perfil, R.drawable.ic_profile),
    PREFERENCES(R.string.title_preferences, R.drawable.ic_preferencias),
    INFORMATION(R.string.title_information, R.drawable.ic_informacoes),
    UNITIES(R.string.title_unities, R.drawable.ic_action_unidades),
    LINKS(R.string.title_links, R.drawable.ic_links),
    CIB(R.string.title_cib, R.drawable.ic_ligar_branco),
    LOGOUT(R.string.text_logoff, R.drawable.ic_logout)
}