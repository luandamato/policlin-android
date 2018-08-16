package br.com.policlinsaude.notHasPassword.view.adapter

import android.content.Context
import com.stepstone.stepper.viewmodel.StepViewModel
import android.support.annotation.NonNull
import android.support.v4.app.FragmentManager
import br.com.policlinsaude.notHasPassword.presenter.NotHasPasswordPresenter
import br.com.policlinsaude.notHasPassword.view.steppers.CreatePasswordFragment
import br.com.policlinsaude.notHasPassword.view.steppers.PersonalDataFragment
import br.com.policlinsaude.notHasPassword.view.steppers.PlanDataFragment
import com.stepstone.stepper.Step
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter

class NotHasPasswordFragmentPagerAdapter(fm: FragmentManager, context: Context,
                                         val presenter: NotHasPasswordPresenter)
    : AbstractFragmentStepAdapter(fm, context) {

    override fun createStep(position: Int): Step =
            when (NotHasPasswordStepperEnum.values()[position]) {
                NotHasPasswordStepperEnum.PERSONAL_DATA -> PersonalDataFragment.newStep()
                NotHasPasswordStepperEnum.PLAN_DATA -> PlanDataFragment.newStep()
                NotHasPasswordStepperEnum.CREATE_PASSWORD -> CreatePasswordFragment.newStep()
            }


    override fun getCount(): Int {
        return NotHasPasswordStepperEnum.values().size
    }

    @NonNull
    override fun getViewModel(position: Int): StepViewModel {
        return StepViewModel.Builder(context)
                .setTitle(NotHasPasswordStepperEnum.values()[position].title)
                .create()
    }
}