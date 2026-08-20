package br.com.policlinsaude.domain.model

data class Person(
    val id: String = "",
    val cpf: String = "",
    val email: String = "",
    val name: String = "",
    val phone: String = "",
    val birthday: String = "",
    val photo: String = ""
)

data class MedicalGuide(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val speciality: String = "",
    val establishment: String = ""
)

data class Plan(
    val id: String = "",
    val register: String = "",
    val order: String = "",
    val contract: String = "",
    val codePlan: String = ""
)
