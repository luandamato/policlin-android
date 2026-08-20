package br.com.policlinsaude.domain.model

/**
 * Modelo de domínio para Guia Médica
 */
data class MedicalGuideDomain(
    val id: String,
    val name: String,
    val description: String,
    val establishmentName: String,
    val cnpj: String,
    val phone: String,
    val address: String,
    val city: String,
    val state: String,
    val zipCode: String,
    val latitude: Double,
    val longitude: Double,
    val distance: Double? = null,
    val isFavorite: Boolean = false
)
