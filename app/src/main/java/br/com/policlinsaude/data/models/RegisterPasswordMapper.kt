package br.com.policlinsaude.data.models

import br.com.policlinsaude.util.extensions.DateHelper

/**
 * Mapper do fluxo de cadastro (não tem senha).
 *
 * Migrado/adaptado de:
 * - `_legacy/.../data/datasource/networking/rest/mapper/RegisterPasswordMapper.kt`
 * - `_legacy/.../mapper/RegisterPasswordMapper.kt` (apresentação)
 *
 * Mantém a mesma lógica do legado: [PresentationPerson] + [PresentationPlan] →
 * corpo JSON do `registerPassword` e parâmetros do `checkPlan`.
 */
object RegisterPasswordMapper {

    /** Extrai os 2 primeiros dígitos do telefone (DDD). */
    fun getCodeArea(phone: String): String =
        if (phone.length < 2) "" else phone.substring(0, 2)

    /** Remove o DDD do telefone (celular sem código de área). */
    fun getPhoneWithoutCodeArea(phone: String): String =
        if (phone.length < 2) "" else phone.substring(2)

    /** Monta o corpo do endpoint `MAPP_ManutencaoBeneficiario` (register password). */
    fun toRegisterPasswordBody(person: PresentationPerson, plan: PresentationPlan): JsonRegisterPasswordBody =
        JsonRegisterPasswordBody().apply {
            register = plan.register
            order = plan.order
            cpf = person.cpf
            contract = plan.contract
            email = person.email
            name = person.name.uppercase()
            codeAreaPhone = getCodeArea(person.phone)
            phone = getPhoneWithoutCodeArea(person.phone)
            validationRegistration = DateHelper.getStringFromDate(DateHelper.FORMAT_DATE_YYYYMMDD, plan.validationRegister)
            password = person.password
            acceptTerm = 1
            birthday = DateHelper.getStringFromDate(DateHelper.FORMAT_DATE_YYYYMMDD, person.birthday)
            avatar = person.photo
            mothersName = person.mothersName
        }
}