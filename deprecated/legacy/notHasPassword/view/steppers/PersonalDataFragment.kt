package br.com.policlinsaude.ui.legacy.notHasPassword.view.steppers

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
import br.com.policlinsaude.notHasPassword.presenter.NotHasPasswordPresenter
import br.com.policlinsaude.notHasPassword.view.NotHasPasswordActivity
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import com.redmadrobot.inputmask.MaskedTextChangedListener
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.fragment_personal_data.*
import java.util.*

class PersonalDataFragment : BaseFragment(), Step {

    companion object {
        private const val EXTRA_PRESENTER = "presenter"
        private const val REGEX_MIN_PHONE = "^[\\s\\S]{15,}\$"
        private const val REGEX_MIN_CPF = "^[\\s\\S]{14,}\$"

        fun newStep(): Step {
            return PersonalDataFragment()
        }
    }

    private lateinit var presenter: NotHasPasswordPresenter

    private var phone: String = InvalidData.UNINITIALIZED.getString()
    private var cpf: String = InvalidData.UNINITIALIZED.getString()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)

        return inflater.inflate(R.layout.fragment_personal_data, container, false)
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
        MaskUtils.applyMaskToView(MaskUtils.PHONE, editTextPhone, object : MaskedTextChangedListener.ValueListener {
            override fun onTextChanged(maskFilled: Boolean, extractedValue: String) {
                phone = extractedValue
            }
        })

        MaskUtils.applyMaskToView(MaskUtils.CPF, editTextCpf, object : MaskedTextChangedListener.ValueListener {
            override fun onTextChanged(maskFilled: Boolean, extractedValue: String) {
                cpf = extractedValue
            }
        })

        MaskUtils.applyMaskToView(MaskUtils.DATE, editTextBirthday, null)
    }

    private fun setOnClickListeners() {
        birthdayCalendarButton.setOnClickListener {
            showDatePicker(editTextBirthday)
        }
    }

    private fun setFields() {
        val person = presenter.getPresentationPerson()
        editTextName.setText(person.name)
        editTextCpf.setText(person.cpf)
        val calendar = Calendar.getInstance()
        calendar.time = person.birthday
        setDateAtEditText(editTextBirthday, calendar)
        editTextMothersName.setText(person.mothersName)
        editTextPhone.setText(person.phone)
        editTextEmail.setText(person.email)
        editTextConfirmEmail.setText(person.email)
    }

    private fun addValidationFields() {
        awesomeValidation.addValidation(editTextName,
                RegexTemplate.NOT_EMPTY, getString(R.string.text_field_required))
        awesomeValidation.addValidation(editTextBirthday,
                RegexTemplate.NOT_EMPTY, getString(R.string.text_field_required))
        awesomeValidation.addValidation(editTextPhone,
                RegexTemplate.TELEPHONE, getString(R.string.text_phone_invalid))
        awesomeValidation.addValidation(editTextPhone,
                RegexTemplate.NOT_EMPTY, getString(R.string.text_field_required))
        awesomeValidation.addValidation(editTextEmail,
                Patterns.EMAIL_ADDRESS, getString(R.string.text_email_invalid))
        awesomeValidation.addValidation(editTextConfirmEmail,
                Patterns.EMAIL_ADDRESS, getString(R.string.text_email_invalid))
        awesomeValidation.addValidation(editTextPhone,
                REGEX_MIN_PHONE, getString(R.string.text_phone_invalid))

        awesomeValidation.addValidation(editTextCpf, { value: String -> Validations.isValidCPF(value) || value.isEmpty() }, getString(R.string.text_cpf_invalid))

        awesomeValidation.addValidation(editTextConfirmEmail, editTextEmail, getString(R.string.text_two_email_invalid))

        awesomeValidation.addValidation(editTextBirthday,
                { value: String -> Validations.isValidDate(value) }, getString(R.string.text_date_invalid))
    }

    override fun onSelected() {
    }

    override fun verifyStep(): VerificationError? {
        if (awesomeValidation.validate()) {
            val person = presenter.getPresentationPerson()
            person.name = editTextName.text.toString()
            person.cpf = cpf
            person.birthday = getDateFromEditText(editTextBirthday)
            person.phone = phone
            person.email = editTextEmail.text.toString()
            person.mothersName = editTextMothersName.text.toString()
            presenter.setPresentationPerson(person)
            return null
        }
        return VerificationError("Validation error")
    }

    override fun onError(error: VerificationError) {

    }
}