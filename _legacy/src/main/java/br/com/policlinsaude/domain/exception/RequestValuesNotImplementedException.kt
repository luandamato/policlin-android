package br.com.policlinsaude.domain.exception

class RequestValuesNotImplementedException(private val className: String) : RuntimeException() {

    override val message: String?
        get() = "Needs to be implement class${className}"
}