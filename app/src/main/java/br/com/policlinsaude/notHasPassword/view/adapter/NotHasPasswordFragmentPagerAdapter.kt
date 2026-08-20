package br.com.policlinsaude.notHasPassword.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import br.com.policlinsaude.notHasPassword.view.steppers.CreatePasswordFragment
import br.com.policlinsaude.notHasPassword.view.steppers.PersonalDataFragment
import br.com.policlinsaude.notHasPassword.view.steppers.PlanDataFragment

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