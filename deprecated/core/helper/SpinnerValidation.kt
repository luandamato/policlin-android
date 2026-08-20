package br.com.policlinsaude.core.helper

import android.widget.Toast
import br.com.policlinsaude.R
import com.basgeekball.awesomevalidation.ValidationHolder
import com.basgeekball.awesomevalidation.utility.custom.CustomErrorReset
import com.basgeekball.awesomevalidation.utility.custom.CustomValidation
import com.basgeekball.awesomevalidation.utility.custom.CustomValidationCallback
import org.angmarch.views.NiceSpinner

class SpinnerValidation: CustomValidation {
    override fun compare(validationHolder: ValidationHolder?): Boolean {
        return (validationHolder!!.view as NiceSpinner).selectedIndex != 0
    }
}

class SpinnerValidationCallback: CustomValidationCallback {
    override fun execute(validationHolder: ValidationHolder?) {
        Toast.makeText(validationHolder!!.view.context,
                R.string.text_field_required, Toast.LENGTH_SHORT).show()
    }
}

class SpinnerCustomErrorReset: CustomErrorReset {
    override fun reset(validationHolder: ValidationHolder?) {}
}