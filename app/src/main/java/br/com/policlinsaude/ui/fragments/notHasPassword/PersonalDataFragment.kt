package br.com.policlinsaude.ui.fragments.notHasPassword

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.policlinsaude.R
import br.com.policlinsaude.databinding.FragmentPersonalDataBinding
import br.com.policlinsaude.ui.activities.notHasPassword.NotHasPasswordActivity
import br.com.policlinsaude.ui.views.BaseFragment
import br.com.policlinsaude.util.helpers.Mask
import br.com.policlinsaude.util.helpers.Validations
import java.util.Calendar

/**
 * Passo 1 do cadastro (não tem senha): Dados Pessoais.
 *
 * Mantém o papel do legado `PersonalDataFragment`, porém lendo/salvando o
 * estado diretamente no [NotHasPasswordActivity.viewModel] (sem Presenter).
 */
class PersonalDataFragment : BaseFragment() {

    companion object {
        private const val REGEX_MIN_PHONE = "^[\\s\\S]{15,}$"

        fun newInstance(): PersonalDataFragment {
            return PersonalDataFragment()
        }
    }

    private var phone: String = ""
    private var cpf: String = ""

    private lateinit var binding: FragmentPersonalDataBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPersonalDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addMasks()
        setFields()
        setOnClickListeners()
    }

    private fun addMasks() {
        Mask.applyToView(
            Mask.PHONE,
            binding.editTextPhone,
            object : com.redmadrobot.inputmask.MaskedTextChangedListener.ValueListener {
                override fun onTextChanged(
                    maskFilled: Boolean,
                    extractedValue: String,
                    formattedValue: String,
                    tailPlaceholder: String
                ) {
                    phone = extractedValue
                }
            }
        )

        Mask.applyToView(
            Mask.CPF,
            binding.editTextCpf,
            object : com.redmadrobot.inputmask.MaskedTextChangedListener.ValueListener {
                override fun onTextChanged(
                    maskFilled: Boolean,
                    extractedValue: String,
                    formattedValue: String,
                    tailPlaceholder: String
                ) {
                    cpf = extractedValue
                }
            }
        )

        Mask.applyToView(Mask.DATE, binding.editTextBirthday)
    }

    private fun setOnClickListeners() {
        binding.birthdayCalendarButton.setOnClickListener {
            showDatePicker(binding.editTextBirthday)
        }
    }

    private fun setFields() {
        val person = (activity as NotHasPasswordActivity).viewModel.getPresentationPerson()
        binding.editTextName.setText(person.name)
        binding.editTextCpf.setText(person.cpf)
        val calendar = Calendar.getInstance()
        calendar.time = person.birthday
        setDateAtEditText(binding.editTextBirthday, calendar)
        binding.editTextMothersName.setText(person.mothersName)
        binding.editTextPhone.setText(person.phone)
        binding.editTextEmail.setText(person.email)
        binding.editTextConfirmEmail.setText(person.email)
    }

    /** Valida os campos e salva no ViewModel. Retorna true se válido. */
    fun validateAndSave(): Boolean {
        if (validateFields()) {
            val person = (activity as NotHasPasswordActivity).viewModel.getPresentationPerson()
            val updated = person.copy(
                name = binding.editTextName.text.toString(),
                cpf = cpf,
                birthday = getDateFromEditText(binding.editTextBirthday).time,
                phone = phone,
                email = binding.editTextEmail.text.toString(),
                mothersName = binding.editTextMothersName.text.toString()
            )
            (activity as NotHasPasswordActivity).viewModel.setPresentationPerson(updated)
            return true
        }
        return false
    }

    private fun validateFields(): Boolean {
        var valid = true
        binding.inputLayoutName.error = null
        binding.inputLayoutCpf.error = null
        binding.inputLayoutBirthday.error = null
        binding.inputLayoutPhone.error = null
        binding.inputLayoutEmail.error = null
        binding.inputLayoutConfirmEmail.error = null

        val name = binding.editTextName.text.toString()
        val birthday = binding.editTextBirthday.text.toString()
        val phoneValue = binding.editTextPhone.text.toString()
        val email = binding.editTextEmail.text.toString()
        val confirmEmail = binding.editTextConfirmEmail.text.toString()

        if (name.isBlank()) {
            binding.inputLayoutName.error = getString(R.string.text_field_required)
            valid = false
        }

        if (cpf.isNotEmpty() && !Validations.isValidCPF(cpf)) {
            binding.inputLayoutCpf.error = getString(R.string.text_cpf_invalid)
            valid = false
        }

        if (birthday.isBlank()) {
            binding.inputLayoutBirthday.error = getString(R.string.text_field_required)
            valid = false
        } else if (!Validations.isValidDate(birthday)) {
            binding.inputLayoutBirthday.error = getString(R.string.text_date_invalid)
            valid = false
        }

        if (phoneValue.isBlank()) {
            binding.inputLayoutPhone.error = getString(R.string.text_field_required)
            valid = false
        } else if (!phoneValue.matches(Regex(REGEX_MIN_PHONE))) {
            binding.inputLayoutPhone.error = getString(R.string.text_phone_invalid)
            valid = false
        }

        if (email.isBlank()) {
            binding.inputLayoutEmail.error = getString(R.string.text_field_required)
            valid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.inputLayoutEmail.error = getString(R.string.text_email_invalid)
            valid = false
        }

        if (confirmEmail.isBlank()) {
            binding.inputLayoutConfirmEmail.error = getString(R.string.text_field_required)
            valid = false
        } else if (confirmEmail != email) {
            binding.inputLayoutConfirmEmail.error = getString(R.string.text_two_email_invalid)
            valid = false
        }

        return valid
    }
}