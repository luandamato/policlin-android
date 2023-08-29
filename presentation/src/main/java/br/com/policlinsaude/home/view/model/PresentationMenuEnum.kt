package br.com.policlinsaude.home.view.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import br.com.policlinsaude.R

/**
 * Created by lmiyagi on 3/19/18.
 */
enum class PresentationMenuEnum(@StringRes val title: Int,
                                @DrawableRes val icon: Int) {

    HOME(R.string.title_home, R.drawable.ic_home_big),
    PROFILE(R.string.title_perfil, R.drawable.ic_profile),
    DELETE(R.string.title_delete_user, R.drawable.ic_trash),
    PREFERENCES(R.string.title_preferences, R.drawable.ic_preferencias),
    INFORMATION(R.string.title_information, R.drawable.ic_informacoes),
    UNITIES(R.string.title_unities, R.drawable.ic_action_unidades),
    LINKS(R.string.title_links, R.drawable.ic_links),
    TELEFONECID(R.string.title_telefonecid, R.drawable.ic_ligar_branco)

}