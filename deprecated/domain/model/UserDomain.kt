package br.com.policlinsaude.domain.model

/**
 * Modelo de domínio para usuário
 * Este modelo representa os dados de um usuário na camada de domínio
 */
data class UserDomain(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val cpf: String,
    val birthDate: String? = null
)
