package br.com.policlinsaude.notHasPassword.view.steppers

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.policlinsaude.R
import br.com.policlinsaude.core.base.BaseFragment
import br.com.policlinsaude.core.helper.InvalidData
import br.com.policlinsaude.core.helper.MaskUtils
import br.com.policlinsaude.core.helper.Validations
import br.com.policlinsaude.databinding.FragmentPersonalDataBinding
import br.com.policlinsaude.notHasPassword.presenter.NotHasPasswordPresenter
import br.com.policlinsaude.notHasPassword.view.NotHasPasswordActivity
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import com.redmadrobot.inputmask.MaskedTextChangedListener
import java.util.*

class PersonalDataFragment : BaseFragment() {

    companion object {
        private const val REGEX_MIN_PHONE = "^[\\s\\S]{15,}\$"

        fun newInstance(): PersonalDataFragment {
            return PersonalDataFragment()
        }
    }

    private lateinit var presenter: NotHasPasswordPresenter

    private var phone: String = InvalidData.UNINITIALIZED.getString()
    private var cpf: String = InvalidData.UNINITIALIZED.getString()

    private lateinit var binding: FragmentPersonalDataBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentPersonalDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = (activity as NotHasPasswordActivity).presenter

        addValidationFields()
        addMaskToFields()
        setFields()
        setOnClickListeners()
    }

    private fun addMaskToFields() {
        MaskUtils.applyMaskToView(
            MaskUtils.PHONE,
            binding.editTextPhone,
            object : MaskedTextChangedListener.ValueListener {

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

        MaskUtils.applyMaskToView(
            MaskUtils.CPF,
            binding.editTextCpf,
            object : MaskedTextChangedListener.ValueListener {

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

        MaskUtils.applyMaskToView(MaskUtils.DATE, binding.editTextBirthday, null)
    }

    private fun setOnClickListeners() {
        binding.birthdayCalendarButton.setOnClickListener {
            showDatePicker(binding.editTextBirthday)
        }
    }

    private fun setFields() {
        val person = presenter.getPresentationPerson()
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

    private fun addValidationFields() {
        awesomeValidation.addValidation(binding.editTextName,
                RegexTemplate.NOT_EMPTY, getString(R.string.text_field_required))
        awesomeValidation.addValidation(binding.editTextBirthday,
                RegexTemplate.NOT_EMPTY, getString(R.string.text_field_required))
        awesomeValidation.addValidation(binding.editTextPhone,
                RegexTemplate.TELEPHONE, getString(R.string.text_phone_invalid))
        awesomeValidation.addValidation(binding.editTextPhone,
                RegexTemplate.NOT_EMPTY, getString(R.string.text_field_required))
        awesomeValidation.addValidation(binding.editTextEmail,
                Patterns.EMAIL_ADDRESS, getString(R.string.text_email_invalid))
        awesomeValidation.addValidation(binding.editTextConfirmEmail,
                Patterns.EMAIL_ADDRESS, getString(R.string.text_email_invalid))
        awesomeValidation.addValidation(binding.editTextPhone,
                REGEX_MIN_PHONE, getString(R.string.text_phone_invalid))

        awesomeValidation.addValidation(binding.editTextCpf, { value: String -> Validations.isValidCPF(value) || value.isEmpty() }, getString(R.string.text_cpf_invalid))

        awesomeValidation.addValidation(binding.editTextConfirmEmail, binding.editTextEmail, getString(R.string.text_two_email_invalid))

        awesomeValidation.addValidation(binding.editTextBirthday,
                { value: String -> Validations.isValidDate(value) }, getString(R.string.text_date_invalid))
    }

    fun validateAndSave(): Boolean {
        if (awesomeValidation.validate()) {
            val person = presenter.getPresentationPerson()
            person.name = binding.editTextName.text.toString()
            person.cpf = cpf
            person.birthday = getDateFromEditText(binding.editTextBirthday)
            person.phone = phone
            person.email = binding.editTextEmail.text.toString()
            person.mothersName = binding.editTextMothersName.text.toString()
            presenter.setPresentationPerson(person)
            return true
        }
        return false
    }
}