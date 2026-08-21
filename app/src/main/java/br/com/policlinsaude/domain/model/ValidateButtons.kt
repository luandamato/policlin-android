package br.com.policlinsaude.domain.model

class ValidateButtons(
    val boleto: Boolean = false,
    val copartFM: String = "",
    val codAcao: Int = -1,
    val IR: Boolean = false,
    val selecaoBeneficiarioAutorizador: Boolean = false,
    val central: Boolean = false,
    val gerarToken: Boolean = false,
    val autorizador: Boolean = false,
    val msgInterna: String = "",
    val msgExterna: String = ""
)

data class ValidateButtonBody(
    val token: String = ""
)