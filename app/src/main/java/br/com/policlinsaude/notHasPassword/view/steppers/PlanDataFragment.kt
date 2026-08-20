package br.com.policlinsaude.notHasPassword.view.steppers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.policlinsaude.R
import br.com.policlinsaude.core.base.BaseFragment
import br.com.policlinsaude.core.helper.DialogHelper
import br.com.policlinsaude.core.helper.IntentHelper
import br.com.policlinsaude.model.PresentationPlan
import br.com.policlinsaude.notHasPassword.presenter.NotHasPasswordPresenter
import br.com.policlinsaude.notHasPassword.view.NotHasPasswordActivity
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import kotlinx.android.synthetic.main.fragment_plan_data.*
import java.util.*


class PlanDataFragment : BaseFragment() {

    companion object {
        fun newInstance(): PlanDataFragment {
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
            IntentHelper.openUrlInBrowser(requireContext(), getString(R.string.url_terms))
        }
    }

    private fun setFields() {
        val plan = presenter.getPresentationPlan()
        editTextRegister.setText(plan.register)
        editTextOrder.setText(plan.order)
        editTextContract.setText(plan.contract)
    }

    private fun addValidationFields() {
        awesomeValidation.addValidation(editTextRegister,
                RegexTemplate.NOT_EMPTY, getString(R.string.text_field_required))
        awesomeValidation.addValidation(editTextOrder,
                RegexTemplate.NOT_EMPTY, getString(R.string.text_field_required))
        awesomeValidation.addValidation(editTextContract,
                RegexTemplate.NOT_EMPTY, getString(R.string.text_field_required))
    }

    fun validateAndSave(onResult: (Boolean) -> Unit) {
        if (awesomeValidation.validate() && checkTermsAccepted()) {
            plan.register = editTextRegister.text.toString()
            plan.order = editTextOrder.text.toString()
            plan.contract = editTextContract.text.toString()
            
            presenter.checkPlan(plan) { success ->
                onResult(success)
            }
        } else {
            onResult(false)
        }
    }

    private fun checkTermsAccepted(): Boolean {
        val accepted = acceptTermsCheckBox.isChecked
        if (!accepted) showTermsNeeded()
        return accepted
    }

    private fun showTermsNeeded() {
        DialogHelper.showDialog(requireContext(),
                R.string.title_terms_of_use,
                R.string.text_must_accept_terms,
                R.string.text_ok)
    }
}
