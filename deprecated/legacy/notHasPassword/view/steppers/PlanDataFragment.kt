package br.com.policlinsaude.ui.legacy.notHasPassword.view.steppers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.policlinsaude.R
import br.com.policlinsaude.core.base.BaseFragment
import br.com.policlinsaude.core.helper.DialogHelper
import br.com.policlinsaude.core.helper.IntentHelper
import br.com.policlinsaude.core.helper.MaskUtils
import br.com.policlinsaude.core.helper.Validations
import br.com.policlinsaude.model.PresentationPlan
import br.com.policlinsaude.notHasPassword.presenter.NotHasPasswordPresenter
import br.com.policlinsaude.notHasPassword.view.NotHasPasswordActivity
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import com.stepstone.stepper.BlockingStep
import com.stepstone.stepper.Step
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.fragment_plan_data.*
import java.util.*


class PlanDataFragment : BaseFragment(), BlockingStep {

    companion object {
        private const val EXTRA_PRESENTER = "presenter"

        fun newStep(): Step {
            return PlanDataFragment()
        }
    }

    private lateinit var presenter: NotHasPasswordPresenter
    private lateinit var plan: PresentationPlan

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_plan_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = (activity as NotHasPasswordActivity).presenter
        plan = presenter.getPresentationPlan()

        addValidationFields()
        setFields()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        termsContainer.setOnClickListener {
            IntentHelper.openUrlInBrowser(context!!, getString(R.string.url_terms))
        }
    }

    private fun setFields() {
        val plan = presenter.getPresentationPlan()
        editTextRegister.setText(plan.register)
        editTextOrder.setText(plan.order)
        editTextContract.setText(plan.contract)
        val calendar = Calendar.getInstance()
        calendar.time = plan.validationRegister
    }

    private fun addValidationFields() {
        awesomeValidation.addValidation(editTextRegister,
                RegexTemplate.NOT_EMPTY, getString(R.string.text_field_required))
        awesomeValidation.addValidation(editTextOrder,
                RegexTemplate.NOT_EMPTY, getString(R.string.text_field_required))
        awesomeValidation.addValidation(editTextContract,
                RegexTemplate.NOT_EMPTY, getString(R.string.text_field_required))
    }

    override fun onSelected() {
    }

    override fun verifyStep(): VerificationError? {
        if (awesomeValidation.validate() && checkTermsAccepted()) {
            plan.register = editTextRegister.text.toString()
            plan.order = editTextOrder.text.toString()
            plan.contract = editTextContract.text.toString()
            return null
        }
        return VerificationError("Validation error")
    }

    private fun checkTermsAccepted(): Boolean {
        val accepted = acceptTermsCheckBox.isChecked
        if (!accepted) showTermsNeeded()
        return accepted
    }

    override fun onError(error: VerificationError) {

    }

    override fun onBackClicked(callback: StepperLayout.OnBackClickedCallback?) {
        callback?.goToPrevStep()
    }

    override fun onCompleteClicked(callback: StepperLayout.OnCompleteClickedCallback?) {
        // nothing
    }

    override fun onNextClicked(callback: StepperLayout.OnNextClickedCallback?) {
        presenter.checkPlan(plan, callback)
    }

    private fun showTermsNeeded() {
        DialogHelper.showDialog(context!!,
                R.string.title_terms_of_use,
                R.string.text_must_accept_terms,
                R.string.text_ok)
    }
}
