package br.com.policlinsaude.ui.fragments.notHasPassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.policlinsaude.R
import br.com.policlinsaude.databinding.FragmentPlanDataBinding
import br.com.policlinsaude.ui.activities.notHasPassword.NotHasPasswordActivity
import br.com.policlinsaude.ui.dialogs.DialogHelper
import br.com.policlinsaude.ui.views.BaseFragment
import br.com.policlinsaude.util.extensions.openBrowser

/**
 * Passo 2 do cadastro (não tem senha): Dados do Plano + aceite de termos.
 *
 * Valida matrícula/ordem/contrato, salva no ViewModel e executa o `checkPlan`
 * (callback com o resultado, igual ao legado).
 */
class PlanDataFragment : BaseFragment() {

    companion object {
        fun newInstance(): PlanDataFragment {
            return PlanDataFragment()
        }
    }

    private lateinit var binding: FragmentPlanDataBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlanDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFields()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.termsContainer.setOnClickListener {
            requireContext().openBrowser(requireContext().getString(R.string.url_terms))
        }
    }

    private fun setFields() {
        val plan = (activity as NotHasPasswordActivity).viewModel.getPresentationPlan()
        binding.editTextRegister.setText(plan.register)
        binding.editTextOrder.setText(plan.order)
        binding.editTextContract.setText(plan.contract)
    }

    /** Valida os campos e chama o checkPlan (callback com o resultado). */
    fun validateAndSave(onResult: (Boolean) -> Unit) {
        if (validateFields() && checkTermsAccepted()) {
            val plan = (activity as NotHasPasswordActivity).viewModel.getPresentationPlan()
            val updated = plan.copy(
                register = binding.editTextRegister.text.toString(),
                order = binding.editTextOrder.text.toString(),
                contract = binding.editTextContract.text.toString()
            )
            (activity as NotHasPasswordActivity).viewModel.setPresentationPlan(updated)
            (activity as NotHasPasswordActivity).viewModel.checkPlan { success ->
                onResult(success)
            }
        } else {
            onResult(false)
        }
    }

    private fun validateFields(): Boolean {
        var valid = true
        binding.inputLayoutRegister.error = null
        binding.inputLayoutOrder.error = null
        binding.inputLayoutContract.error = null

        if (binding.editTextRegister.text.toString().isBlank()) {
            binding.inputLayoutRegister.error = getString(R.string.text_field_required)
            valid = false
        }
        if (binding.editTextOrder.text.toString().isBlank()) {
            binding.inputLayoutOrder.error = getString(R.string.text_field_required)
            valid = false
        }
        if (binding.editTextContract.text.toString().isBlank()) {
            binding.inputLayoutContract.error = getString(R.string.text_field_required)
            valid = false
        }
        return valid
    }

    private fun checkTermsAccepted(): Boolean {
        val accepted = binding.acceptTermsCheckBox.isChecked
        if (!accepted) showTermsNeeded()
        return accepted
    }

    private fun showTermsNeeded() {
        DialogHelper.showDialog(
            requireContext(),
            R.string.title_terms_of_use,
            R.string.text_must_accept_terms,
            R.string.text_ok
        )
    }
}