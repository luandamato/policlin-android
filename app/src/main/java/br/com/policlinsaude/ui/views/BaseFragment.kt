package br.com.policlinsaude.ui.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import br.com.policlinsaude.util.extensions.pickDate
import java.util.Calendar

/**
 * Fragment base da nova arquitetura (sem Dagger/DI).
 * Migrado/adaptado de `_legacy/.../core/base/BaseFragment.kt` mantendo
 * os helpers de data (preencher/ler/lançar DatePicker em [EditText]).
 */
abstract class BaseFragment : Fragment() {

    private val dateFormatDefault = "dd/MM/yyyy"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun setDateAtEditText(editText: EditText, calendar: Calendar) {
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
        editText.setText(String.format("%02d/%02d/%04d", day, month, year))
    }

    fun getDateFromEditText(editText: EditText): Calendar {
        val calendar = Calendar.getInstance()
        val parts = editText.text.toString().split("/")
        if (parts.size == 3) {
            calendar.set(
                parts[2].toIntOrNull() ?: calendar.get(Calendar.YEAR),
                (parts[1].toIntOrNull() ?: 0) - 1,
                parts[0].toIntOrNull() ?: 1
            )
        }
        return calendar
    }

    fun showDatePicker(editText: EditText, minDate: Calendar? = null, maxDate: Calendar? = null) {
        val calendar = getDateFromEditText(editText)
        requireContext().pickDate(
            initialDate = calendar,
            minDate = minDate,
            maxDate = maxDate
        ) { day, month, year ->
            val c = Calendar.getInstance().apply {
                set(year, month - 1, day)
            }
            setDateAtEditText(editText, c)
        }
    }
}