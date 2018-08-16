package br.com.policlinsaude.units.view.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import br.com.policlinsaude.model.PresentationEstablishment
import br.com.policlinsaude.model.PresentationQualification
import br.com.policlinsaude.units.view.UnitsFragment

class UnitsPageAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {


    private var units: List<Pair<String, List<PresentationEstablishment>>> = listOf()
    private var qualifications: List<PresentationQualification> = listOf()

  //  override fun getItem(position: Int): Fragment = OwnNetworkFragment.newInstance(units[position].second, qualifications)
  override fun getItem(position: Int): Fragment = UnitsFragment.newInstance(units[position].second, qualifications)

    fun setUnits(units: List<Pair<String, List<PresentationEstablishment>>>, qualifications: MutableList<PresentationQualification>) {
        this.units = units
        this.qualifications = qualifications
        notifyDataSetChanged()
    }

    override fun getCount(): Int = units.size

    override fun getPageTitle(position: Int): CharSequence? {
        return units[position].second[0].city
    }
}