package br.com.policlinsaude.data.services

import br.com.policlinsaude.data.models.*
import retrofit2.Call
import retrofit2.http.*

/**
 * Interface Retrofit consolidada com todos os endpoints da aplicação
 * Sem Hilt - injeção manual ou singleton
 */
interface ApiService {

    // ============= Authentication =============

    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET("auth/me")
    fun getCurrentUser(@Header("Authorization") token: String): Call<UserProfile>

    @POST("auth/logout")
    fun logout(@Header("Authorization") token: String): Call<String>

    @POST("auth/forgot-password")
    fun forgotPassword(@Body request: ForgotPasswordRequest): Call<String>

    @POST("auth/reset-password")
    fun resetPassword(@Body request: PasswordResetRequest): Call<String>

    // ============= Medical Guide =============

    @GET("medical-guides")
    fun getMedicalGuides(
        @Header("Authorization") token: String,
        @Query("specialty") specialty: String? = null,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Call<ApiResponse<List<MedicalGuide>>>

    @GET("medical-guides/{id}")
    fun getMedicalGuideDetail(
        @Header("Authorization") token: String,
        @Path("id") guideId: String
    ): Call<ApiResponse<MedicalGuide>>

    @POST("medical-guides/{id}/favorite")
    fun addToFavorite(
        @Header("Authorization") token: String,
        @Path("id") guideId: String
    ): Call<String>

    @DELETE("medical-guides/{id}/favorite")
    fun removeFromFavorite(
        @Header("Authorization") token: String,
        @Path("id") guideId: String
    ): Call<String>

    // ============= Plans =============

    @GET("plans")
    fun getPlans(
        @Header("Authorization") token: String
    ): Call<ApiResponse<List<Plan>>>

    @GET("plans/{id}")
    fun getPlanDetail(
        @Header("Authorization") token: String,
        @Path("id") planId: String
    ): Call<ApiResponse<Plan>>

    // ============= Insurance Card =============

    @GET("insurance-card")
    fun getInsuranceCard(
        @Header("Authorization") token: String
    ): Call<ApiResponse<InsurancePlan>>

    @POST("insurance-card/photo")
    fun uploadCardPhoto(
        @Header("Authorization") token: String,
        @Body data: Map<String, String>
    ): Call<String>

    // ============= Authorizations (Medical Guide Requests) =============

    @GET("authorizations")
    fun getAuthorizations(
        @Header("Authorization") token: String,
        @Query("status") status: String? = null,
        @Query("page") page: Int = 1
    ): Call<ApiResponse<List<AuthorizationGuide>>>

    @GET("authorizations/{id}")
    fun getAuthorizationDetail(
        @Header("Authorization") token: String,
        @Path("id") authId: String
    ): Call<ApiResponse<AuthorizationGuide>>

    @POST("authorizations")
    fun requestAuthorization(
        @Header("Authorization") token: String,
        @Body request: Map<String, Any>
    ): Call<ApiResponse<AuthorizationGuide>>

    // ============= Benefits =============

    @GET("benefits")
    fun getBenefits(
        @Header("Authorization") token: String
    ): Call<ApiResponse<List<Benefit>>>

    // ============= Invoices/Financeiro =============

    @GET("invoices")
    fun getInvoices(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1
    ): Call<ApiResponse<List<Invoice>>>

    // ============= Notifications =============

    @GET("notifications")
    fun getNotifications(
        @Header("Authorization") token: String,
        @Query("unread") unread: Boolean? = null
    ): Call<ApiResponse<List<Notification>>>

    @PUT("notifications/{id}/read")
    fun markNotificationAsRead(
        @Header("Authorization") token: String,
        @Path("id") notificationId: String
    ): Call<String>
}

// ============= Request Bodies =============

data class ForgotPasswordRequest(
    val email: String
)

data class PasswordResetRequest(
    val token: String,
    val newPassword: String,
    val confirmPassword: String
)
