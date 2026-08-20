package br.com.policlinsaude.domain.usecase

import br.com.policlinsaude.domain.model.LoginRequest
import br.com.policlinsaude.domain.model.LoginResponse
import br.com.policlinsaude.domain.model.UserProfile
import br.com.policlinsaude.domain.repository.AuthRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

/**
 * Use case for user login
 */
class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    fun execute(loginRequest: LoginRequest): Single<LoginResponse> {
        return authRepository.login(loginRequest)
    }
}

/**
 * Use case for getting current user profile
 */
class GetCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    fun execute(): Single<UserProfile> {
        return authRepository.getCurrentUser()
    }
}

/**
 * Use case for checking if token is valid
 */
class CheckValidTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    fun execute(): Single<Boolean> {
        return authRepository.hasValidToken()
    }
}

/**
 * Use case for logout
 */
class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    fun execute(): Completable {
        return authRepository.logout()
    }
}

/**
 * Use case for requesting password reset
 */
class ForgotPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    fun execute(email: String): Single<String> {
        return authRepository.requestPasswordReset(email)
    }
}

/**
 * Use case for resetting password
 */
class ResetPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    fun execute(token: String, newPassword: String): Single<String> {
        return authRepository.resetPassword(token, newPassword)
    }
}
