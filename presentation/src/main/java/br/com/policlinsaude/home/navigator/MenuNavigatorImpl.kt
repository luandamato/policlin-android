package br.com.policlinsaude.home.navigator

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import br.com.policlinsaude.R
import br.com.policlinsaude.home.view.HomeFragment
import br.com.policlinsaude.home.view.MenuActivity
import br.com.policlinsaude.informations.InformationsFragment
import br.com.policlinsaude.links.LinksFragment
import br.com.policlinsaude.login.view.LoginActivity
import br.com.policlinsaude.ownNetwork.view.OwnNetworkActivity
import br.com.policlinsaude.perfil.view.PerfilFragment
import br.com.policlinsaude.preferences.view.PreferencesFragment
import br.com.policlinsaude.units.view.UnitsActivity
import br.com.policlinsaude.units.view.UnitsFragment

class MenuNavigatorImpl(private val activity: MenuActivity,
                        private val fragmentManager: FragmentManager
) : MenuNavigator {


    //Andre
    override fun goToUnits() {
     //   goToFragment(UnitsFragment.newInstance())
       UnitsActivity.start(activity)
       // OwnNetworkActivity.start(activity)
    }
    override fun goToPerfil() {
        goToFragment(PerfilFragment.newInstance())
    }

    override fun goToHome() {
        goToFragmentAndPopBackstack(HomeFragment.newInstance())
    }

    override fun goToLogin() {
        LoginActivity.start(activity)
    }

    override fun goToPreferences() {
        goToFragment(PreferencesFragment.newInstance())
    }

    override fun goToInformations() {
        goToFragment(InformationsFragment.newInstance())
    }

    override fun goToLinks() {
        goToFragment(LinksFragment.newInstance())
    }

    override fun goToCallIntent() {
        activity.startActivity(Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:01221392599")))
    }

    private fun goToFragment(fragment: Fragment) {
        fragmentManager.beginTransaction()
                .replace(R.id.content_layout, fragment)
                .addToBackStack(null)
                .commit()
    }

    private fun goToFragmentAndPopBackstack(fragment: Fragment) {
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        goToFragment(fragment)
    }



}