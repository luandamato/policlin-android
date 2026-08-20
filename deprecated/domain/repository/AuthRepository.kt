package br.com.policlinsaude.domain.repository

import br.com.policlinsaude.domain.model.LoginRequest
import br.com.policlinsaude.domain.model.LoginResponse
import br.com.policlinsaude.domain.model.UserProfile
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Repository interface for authentication operations
 */
interface AuthRepository {

    /**
     * Perform user login
     */
    fun login(loginRequest: LoginRequest): Single<LoginResponse>

    /**
     * Get current user profile
     */
    fun getCurrentUser(): Single<UserProfile>

    /**
     * Check if user has valid token
     */
    fun hasValidToken(): Single<Boolean>

    /**
     * Logout user (clear session)
     */
    fun logout(): Completable

    /**
     * Request password reset (forgot password)
     */
    fun requestPasswordReset(email: String): Single<String>

    /**
     * Reset password with token
     */
    fun resetPassword(token: String, newPassword: String): Single<String>

    /**
     * Save authentication token locally
     */
    fun saveToken(token: String): Completable

    /**
     * Get stored authentication token
     */
    fun getToken(): Single<String>

    /**
     * Clear stored authentication data
     */
    fun clearAuthData(): Completable
}
