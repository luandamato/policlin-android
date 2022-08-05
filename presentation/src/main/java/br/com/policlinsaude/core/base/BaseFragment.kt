package br.com.policlinsaude.core.base

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import br.com.policlinsaude.R
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Clock
import java.util.*


abstract class BaseFragment : Fragment() {

    companion object {
        private const val FORMAT_DATE_DEFAULT = "dd/MM/yyyy"
    }

    lateinit var awesomeValidation: AwesomeValidation

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        awesomeValidation = AwesomeValidation(ValidationStyle.BASIC)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun setDateAtEditText(editText: EditText, calendar: Calendar) {
        val calendarioDate = Calendar.getInstance().time
        val simpleDateFormat = SimpleDateFormat(FORMAT_DATE_DEFAULT, Locale.getDefault())
        val dataPraTexto = getString(R.string.text_date, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR))
        val formatoDataSimples = simpleDateFormat.format(calendarioDate)
        println(formatoDataSimples) //
        println(dataPraTexto)

        if (formatoDataSimples == dataPraTexto){
            editText.setText("")
        }
        else editText.setText(getString(R.string.text_date, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR)))
    }

    fun getDateFromEditText(editText: EditText): Date {
        val calendar = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat(FORMAT_DATE_DEFAULT, Locale.getDefault())
        try {
            val date = simpleDateFormat.parse(editText.text.toString())
            calendar.time = date
        } catch (parseException: ParseException) {
        }

        return calendar.time
    }

    fun showDatePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        calendar.time = getDateFromEditText(editText)

        val listener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            setDateAtEditText(editText, calendar)
        }
        val datePickerDialog = DatePickerDialog(requireContext(), listener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.show()
    }

}