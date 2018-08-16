package br.com.policlinsaude.ownNetwork.view.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.util.Log
import android.view.View
import br.com.policlinsaude.model.PresentationEstablishment
import br.com.policlinsaude.model.PresentationQualification
import br.com.policlinsaude.ownNetwork.view.OwnNetworkFragment
import android.view.ViewGroup



class OwnNetworkPageAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {


    private var ownNetworks: List<Pair<String, List<PresentationEstablishment>>> = listOf()
    private var qualifications: List<PresentationQualification> = listOf()

    override fun getItem(position: Int): Fragment = OwnNetworkFragment.newInstance(ownNetworks[position].second, qualifications)

    fun setOwnNetworks(ownNetworks: List<Pair<String, List<PresentationEstablishment>>>, qualifications: MutableList<PresentationQualification>) {
        this.ownNetworks = ownNetworks
        this.qualifications = qualifications
        notifyDataSetChanged()
    }

    override fun getCount(): Int = ownNetworks.size

    override fun getPageTitle(position: Int): CharSequence? {
        return ownNetworks[position].second[0].city
    }

    //utilizado para sanar o bug do viewpager
    override fun getItemPosition(`object`: Any): Int {
        //return super.getItemPosition(`object`)

        return POSITION_NONE
    }


}