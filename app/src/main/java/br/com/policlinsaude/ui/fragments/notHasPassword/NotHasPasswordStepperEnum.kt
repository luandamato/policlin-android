package br.com.policlinsaude.ui.fragments.notHasPassword

import br.com.policlinsaude.R

/** Passos do stepper de cadastro (não tem senha). */
enum class NotHasPasswordStepperEnum(val title: Int) {
    PERSONAL_DATA(R.string.title_personal_data),
    PLAN_DATA(R.string.title_plan_data),
    CREATE_PASSWORD(R.string.title_create_password)
}