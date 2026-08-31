package br.com.policlinsaude.util.helpers

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.redmadrobot.inputmask.MaskedTextChangedListener
import com.redmadrobot.inputmask.helper.Mask as InputMask
import com.redmadrobot.inputmask.model.CaretString

/**
 * Máscaras de texto reutilizáveis.
 * Migrado/adaptado de `_legacy/.../core/helper/{MaskUtils,MaskAndre,MascaraAndre}.kt`.
 */
object Mask {

    const val CPF = "[000].[000].[000]-[00]"
    const val PHONE = "([00]) [00000]-[0000]"
    const val DATE = "[00]/[00]/[0000]"
    const val ZIPCODE = "[00000]-[000]"

    /** Remove caracteres de máscara de uma string. */
    fun unmask(value: String): String = value
        .replace(".", "")
        .replace("-", "")
        .replace("/", "")
        .replace("(", "")
        .replace(")", "")
        .replace(" ", "")

    /**
     * Aplica máscara via input-mask (lib) a um EditText.
     * Recomendado para CPF, telefone, CEP e data.
     */
    fun applyToView(mask: String, editText: EditText, valueListener: MaskedTextChangedListener.ValueListener? = null) {
        val listener = MaskedTextChangedListener(mask, true, editText, null, valueListener)
        editText.addTextChangedListener(listener)
    }

    /** Aplica máscara a uma string (sem edição). */
    fun applyToString(mask: String, text: String): String =
        InputMask(mask).apply(
            CaretString(
                string = text,
                caretPosition = text.length,
                caretGravity = CaretString.CaretGravity.FORWARD(false)
            )
        ).formattedText.string

    /**
     * Aplica máscara incremental em EditText (TextWatcher).
     * Usa `#` como placeholder para dígitos na máscara.
     */
    fun insert(mask: String, editText: EditText): TextWatcher =
        object : TextWatcher {
            private var isUpdating = false
            private var old = ""

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val str = unmask(s.toString())
                var mascara = ""
                if (isUpdating) {
                    old = str
                    isUpdating = false
                    return
                }

                var i = 0
                for (m in mask.toCharArray()) {
                    if (m != '#' && str.length > old.length) {
                        mascara += m
                        continue
                    }
                    try {
                        mascara += str[i]
                    } catch (e: Exception) {
                        break
                    }
                    i++
                }

                isUpdating = true
                editText.setText(mascara)
                editText.setSelection(mascara.length)
            }

            override fun afterTextChanged(s: Editable) = Unit
        }
}