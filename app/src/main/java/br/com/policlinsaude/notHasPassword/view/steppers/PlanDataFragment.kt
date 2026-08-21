package br.com.policlinsaude.notHasPassword.view.steppers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.policlinsaude.R
import br.com.policlinsaude.core.base.BaseFragment
import br.com.policlinsaude.core.helper.DialogHelper
import br.com.policlinsaude.core.helper.IntentHelper
import br.com.policlinsaude.databinding.FragmentPlanDataBinding
import br.com.policlinsaude.model.PresentationPlan
import br.com.policlinsaude.notHasPassword.presenter.NotHasPasswordPresenter
import br.com.policlinsaude.notHasPassword.view.NotHasPasswordActivity
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import java.util.*


class PlanDataFragment : BaseFragment() {

    companion object {
        fun newInstance(): PlanDataFragment {
            return PlanDataFragment()
        }
    }

    private lateinit var presenter: NotHasPasswordPresenter
    private lateinit var plan: PresentationPlan

    private lateinit var binding: FragmentPlanDataBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentPlanDataBinding.inflate(inflater, container, false)
        return binding.root
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
        binding.termsContainer.setOnClickListener {
            IntentHelper.openUrlInBrowser(requireContext(), getString(R.string.url_terms))
        }
    }

    private fun setFields() {
        val plan = presenter.getPresentationPlan()
        binding.editTextRegister.setText(plan.register)
        binding.editTextOrder.setText(plan.order)
        binding.editTextContract.setText(plan.contract)
    }

    private fun addValidationFields() {
        awesomeValidation.addValidation(binding.editTextRegister,
                RegexTemplate.NOT_EMPTY, getString(R.string.text_field_required))
        awesomeValidation.addValidation(binding.editTextOrder,
                RegexTemplate.NOT_EMPTY, getString(R.string.text_field_required))
        awesomeValidation.addValidation(binding.editTextContract,
                RegexTemplate.NOT_EMPTY, getString(R.string.text_field_required))
    }

    fun validateAndSave(onResult: (Boolean) -> Unit) {
        if (awesomeValidation.validate() && checkTermsAccepted()) {
            plan.register = binding.editTextRegister.text.toString()
            plan.order = binding.editTextOrder.text.toString()
            plan.contract = binding.editTextContract.text.toString()
            
            presenter.checkPlan(plan) { success ->
                onResult(success)
            }
        } else {
            onResult(false)
        }
    }

    private fun checkTermsAccepted(): Boolean {
        val accepted = binding.acceptTermsCheckBox.isChecked
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
