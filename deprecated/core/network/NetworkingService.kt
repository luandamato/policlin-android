package br.com.policlinsaude.core.network

import br.com.policlinsaude.domain.model.LoginRequest
import br.com.policlinsaude.domain.model.LoginResponse
import br.com.policlinsaude.domain.model.UserProfile
import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Header
import retrofit2.http.Query

/**
 * Interface para requisições de rede usando Retrofit
 * Base URL: https://api.policlinsaude.com.br/
 */
interface NetworkingService {

    // ============= Authentication Endpoints =============

    /**
     * POST /auth/login
     * Realiza o login do usuário
     */
    @POST("auth/login")
    fun login(@Body loginRequest: LoginRequest): Single<LoginResponse>

    /**
     * GET /auth/me
     * Obtém os dados do usuário logado
     */
    @GET("auth/me")
    fun getCurrentUser(
        @Header("Authorization") token: String
    ): Single<UserProfile>

    /**
     * POST /auth/logout
     * Realiza logout do usuário
     */
    @POST("auth/logout")
    fun logout(
        @Header("Authorization") token: String
    ): Single<String>

    /**
     * POST /auth/forgot-password
     * Solicita reset de senha
     */
    @POST("auth/forgot-password")
    fun forgotPassword(
        @Body request: ForgotPasswordBody
    ): Single<String>

    /**
     * POST /auth/reset-password
     * Completa o reset de senha
     */
    @POST("auth/reset-password")
    fun resetPassword(
        @Body request: ResetPasswordBody
    ): Single<String>

    // ============= Medical Guide Endpoints =============

    @GET("medical-guides")
    fun getMedicalGuides(
        @Header("Authorization") token: String,
        @Query("proUF") proUF: String,
        @Query("prsCod") prsCod: String,
        @Query("proCls") proCls: String,
        @Query("proCod") proCod: String
    ): Flowable<MedicalGuidesResponse>

    @GET("plans")
    fun getPlans(
        @Header("Authorization") token: String
    ): Flowable<PlansResponse>
}

// ============= Request/Response Models =============

data class ForgotPasswordBody(
    val email: String
)

data class ResetPasswordBody(
    val token: String,
    val newPassword: String,
    val confirmPassword: String
)

data class MedicalGuidesResponse(
    val data: List<Any>?,
    val message: String?
)

data class PlansResponse(
    val data: List<Any>?,
    val message: String?
)
