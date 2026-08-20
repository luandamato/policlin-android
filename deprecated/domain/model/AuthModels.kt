package br.com.policlinsaude.domain.model

/**
 * Request model for user login
 */
data class LoginRequest(
    val register: String,      // Matrícula/CPF
    val order: String,         // Sequência/Order
    val password: String,      // Senha
    val firebaseToken: String, // Firebase FCM token
    val osVersion: String      // Android version
)

/**
 * Response model for successful login
 */
data class LoginResponse(
    val token: String,
    val refreshToken: String? = null,
    val user: UserProfile? = null
)

/**
 * User profile information
 */
data class UserProfile(
    val id: String,
    val name: String,
    val email: String,
    val register: String,      // Matrícula
    val phoneNumber: String? = null,
    val documentNumber: String? = null,
    val profileImage: String? = null,
    val plans: List<InsurancePlan>? = null
)

/**
 * Insurance plan for user
 */
data class InsurancePlan(
    val id: String,
    val name: String,
    val operator: String,
    val cardNumber: String,
    val validity: String
)

/**
 * Forgot password request
 */
data class ForgotPasswordRequest(
    val email: String
)

/**
 * Password reset request (for completing password reset flow)
 */
data class PasswordResetRequest(
    val token: String,
    val newPassword: String,
    val confirmPassword: String
)

/**
 * API response wrapper for generic responses
 */
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val message: String? = null,
    val error: String? = null
)
