package br.com.policlinsaude.ui.feature.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.policlinsaude.data.repository.UserRepository
import br.com.policlinsaude.domain.model.UserDomain
import br.com.policlinsaude.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {

    private val _userProfile = MutableLiveData<UserDomain>()
    val userProfile: LiveData<UserDomain> = _userProfile

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun loadUserProfile(token: String) {
        _isLoading.value = true
        disposables.add(
            userRepository.getUserProfile(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { user ->
                        _userProfile.value = user
                        _isLoading.value = false
                    },
                    { throwable ->
                        _errorMessage.value = throwable.message ?: "Erro desconhecido"
                        _isLoading.value = false
                    }
                )
        )
    }

    fun updateUserProfile(token: String, user: UserDomain) {
        _isLoading.value = true
        disposables.add(
            userRepository.updateUserProfile(token, user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { updatedUser ->
                        _userProfile.value = updatedUser
                        _isLoading.value = false
                    },
                    { throwable ->
                        _errorMessage.value = throwable.message ?: "Erro ao atualizar"
                        _isLoading.value = false
                    }
                )
        )
    }
}
