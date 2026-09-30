package br.com.policlinsaude.ui.activities.ownNetwork

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import br.com.policlinsaude.data.models.PresentationEstablishment
import br.com.policlinsaude.data.models.PresentationQualification

/**
 * Adapter de páginas (tabs) de Rede Propia usando ViewPager2.
 *
 * Migrado/adaptado de `_legacy/.../ownNetwork/view/adapter/OwnNetworkPageAdapter.kt`
 * (que usaba `FragmentStatePagerAdapter` de ViewPager 1). Una página por grupo (ciudad).
 */
class OwnNetworkPageAdapter(
    fragmentActivity: FragmentActivity,
    private val groups: List<OwnNetworkGroup>,
    private val qualifications: List<PresentationQualification>,
    private val onItemClick: (PresentationEstablishment) -> Unit
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = groups.size

    override fun createFragment(position: Int): Fragment =
        OwnNetworkFragment.newInstance(
            groups[position].establishments.toList(),
            qualifications,
            onItemClick
        )
}