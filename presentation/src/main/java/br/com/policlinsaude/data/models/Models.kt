package br.com.policlinsaude.data.models

/**
 * Data classes consolidados de toda a aplicação
 */

// ============= Authentication =============

data class LoginRequest(
    val register: String,
    val order: String,
    val password: String,
    val firebaseToken: String,
    val osVersion: String
)

data class LoginResponse(
    val token: String,
    val refreshToken: String? = null,
    val user: UserProfile? = null
)

data class UserProfile(
    val id: String,
    val name: String,
    val email: String,
    val register: String,
    val phoneNumber: String? = null,
    val documentNumber: String? = null,
    val profileImage: String? = null,
    val plans: List<InsurancePlan>? = null
)

data class InsurancePlan(
    val id: String,
    val name: String,
    val operator: String,
    val cardNumber: String,
    val validity: String
)

// ============= Medical Guide =============

data class MedicalGuide(
    val id: String,
    val name: String,
    val specialty: String,
    val crm: String,
    val location: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val isFavorite: Boolean = false
)

data class MedicalGuideOptions(
    val id: String,
    val name: String,
    val options: List<String>
)

data class Plan(
    val id: String,
    val name: String,
    val operator: String,
    val coverage: String? = null
)

// ============= Other Models =============

data class Notification(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: Long,
    val isRead: Boolean = false
)

data class Benefit(
    val id: String,
    val name: String,
    val description: String?,
    val value: Double? = null
)

data class Invoice(
    val id: String,
    val date: String,
    val amount: Double,
    val status: String,
    val dueDate: String? = null
)

data class AuthorizationGuide(
    val id: String,
    val protocolNumber: String,
    val patientName: String,
    val status: String,
    val procedureName: String,
    val validityDate: String
)

// ============= Generic API Response =============

data class ApiResponse<T>(
    val success: Boolean = true,
    val data: T? = null,
    val message: String? = null,
    val error: String? = null,
    val code: Int? = null
)

// ============= Error Model =============

data class ApiError(
    val code: Int,
    override val message: String,
    val details: String? = null
) : Exception(message)
