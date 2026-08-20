package br.com.policlinsaude.ui.auth.login

import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.policlinsaude.domain.model.LoginRequest
import br.com.policlinsaude.domain.model.UserProfile
import br.com.policlinsaude.domain.usecase.CheckValidTokenUseCase
import br.com.policlinsaude.domain.usecase.GetCurrentUserUseCase
import br.com.policlinsaude.domain.usecase.LoginUseCase
import br.com.policlinsaude.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

/**
 * ViewModel for Login screen
 * Manages authentication state and login operations
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val checkValidTokenUseCase: CheckValidTokenUseCase
) : BaseViewModel() {

    // Login state
    sealed class LoginState {
        object Idle : LoginState()
        object Loading : LoginState()
        object CheckingToken : LoginState()
        data class Success(val user: UserProfile) : LoginState()
        data class Error(val message: String) : LoginState()
        object InvalidCredentials : LoginState()
    }

    // Password visibility state
    sealed class PasswordVisibility {
        object Hidden : PasswordVisibility()
        object Visible : PasswordVisibility()
    }

    private val _loginState = MutableLiveData<LoginState>(LoginState.Idle)
    val loginState: LiveData<LoginState> get() = _loginState

    private val _passwordVisibility = MutableLiveData<PasswordVisibility>(PasswordVisibility.Hidden)
    val passwordVisibility: LiveData<PasswordVisibility> get() = _passwordVisibility

    private val _navigateToHome = MutableLiveData<Boolean>(false)
    val navigateToHome: LiveData<Boolean> get() = _navigateToHome

    /**
     * Check if user already has a valid token (for splash/launch screen)
     */
    fun checkValidToken() {
        _loginState.value = LoginState.CheckingToken
        disposables.add(
            checkValidTokenUseCase.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = { hasToken ->
                        if (hasToken) {
                            // User has token, get their data
                            getCurrentUserData()
                        } else {
                            // No token, show login form
                            _loginState.value = LoginState.Idle
                        }
                    },
                    onError = {
                        _loginState.value = LoginState.Idle
                    }
                )
        )
    }

    /**
     * Perform login with credentials
     */
    fun login(register: String, order: String, password: String, firebaseToken: String) {
        // Validate inputs
        if (register.isEmpty() || order.isEmpty() || password.isEmpty()) {
            _loginState.value = LoginState.InvalidCredentials
            return
        }

        _loginState.value = LoginState.Loading

        val loginRequest = LoginRequest(
            register = register,
            order = order,
            password = password,
            firebaseToken = firebaseToken,
            osVersion = Build.VERSION.RELEASE
        )

        disposables.add(
            loginUseCase.execute(loginRequest)
                .flatMap { _ ->
                    // After login, get user data
                    getCurrentUserUseCase.execute()
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = { user ->
                        _loginState.value = LoginState.Success(user)
                        _navigateToHome.value = true
                    },
                    onError = { error ->
                        val errorMessage = error.message
                            ?: "Login failed. Please try again."
                        _loginState.value = LoginState.Error(errorMessage)
                    }
                )
        )
    }

    /**
     * Get current user data after successful login
     */
    private fun getCurrentUserData() {
        disposables.add(
            getCurrentUserUseCase.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = { user ->
                        _loginState.value = LoginState.Success(user)
                        _navigateToHome.value = true
                    },
                    onError = {
                        // Failed to get user data, show login form
                        _loginState.value = LoginState.Idle
                    }
                )
        )
    }

    /**
     * Toggle password visibility
     */
    fun togglePasswordVisibility() {
        _passwordVisibility.value = when (_passwordVisibility.value) {
            PasswordVisibility.Hidden -> PasswordVisibility.Visible
            PasswordVisibility.Visible -> PasswordVisibility.Hidden
            else -> PasswordVisibility.Hidden
        }
    }

    /**
     * Clear any error messages
     */
    fun clearError() {
        if (_loginState.value is LoginState.Error) {
            _loginState.value = LoginState.Idle
        }
    }

    /**
     * Reset navigation flag
     */
    fun navigationHandled() {
        _navigateToHome.value = false
    }
}
