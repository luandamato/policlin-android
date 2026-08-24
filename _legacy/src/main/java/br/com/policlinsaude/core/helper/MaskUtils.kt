package br.com.policlinsaude.core.helper

import android.widget.EditText
import com.redmadrobot.inputmask.MaskedTextChangedListener
import com.redmadrobot.inputmask.helper.Mask
import com.redmadrobot.inputmask.model.CaretString


object MaskUtils {
    const val CPF = "[000].[000].[000]-[00]"
    const val PHONE = "([00]) [00000]-[0000]"
    const val DATE = "[00]/[00]/[0000]"

    fun applyMaskToView(mask: String, editText: EditText, valueListener: MaskedTextChangedListener.ValueListener?) {
        val listener = MaskedTextChangedListener(
                mask,
                true,
                editText,
                null,
                valueListener)
        editText.addTextChangedListener(listener)
    }

    fun applyMaskToString(mask: String, text: String): String {
        return Mask(mask).apply(
            CaretString(
                string = text,
                caretPosition = text.length,
                caretGravity = CaretString.CaretGravity.FORWARD(false)
            )
        ).formattedText.string
    }
}