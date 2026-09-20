package br.com.policlinsaude.ui.fragments.notHasPassword

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/** Adaptador do stepper de cadastro (não tem senha) via ViewPager2. */
class NotHasPasswordFragmentPagerAdapter(fragmentActivity: FragmentActivity)
    : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return NotHasPasswordStepperEnum.values().size
    }

    override fun createFragment(position: Int): Fragment {
        return when (NotHasPasswordStepperEnum.values()[position]) {
            NotHasPasswordStepperEnum.PERSONAL_DATA -> PersonalDataFragment.newInstance()
            NotHasPasswordStepperEnum.PLAN_DATA -> PlanDataFragment.newInstance()
            NotHasPasswordStepperEnum.CREATE_PASSWORD -> CreatePasswordFragment.newInstance()
        }
    }
}