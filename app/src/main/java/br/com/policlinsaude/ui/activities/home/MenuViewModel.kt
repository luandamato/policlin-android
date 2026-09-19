package br.com.policlinsaude.ui.activities.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.policlinsaude.data.local.SessionManager
import br.com.policlinsaude.data.repositories.AppRepository
import br.com.policlinsaude.domain.models.Person
import br.com.policlinsaude.util.helpers.SingleLiveEvent
import br.com.policlinsaude.utils.LogManager
import kotlinx.coroutines.launch

/**
 * Eventos únicos del shell Home ([MenuActivity]).
 */
sealed interface MenuEvent {
    data object ShowLoginDialog : MenuEvent
    data object LoggedOut : MenuEvent
    data class ShowError(val message: String) : MenuEvent
    data class ShowUpdateApp(val message: String) : MenuEvent
}

/**
 * ViewModel del shell (drawer + barra superior).
 * Migrado de `_legacy/.../home/presenter/MenuPresenterImpl.kt`.
 */
class MenuViewModel(
    private val appRepository: AppRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _person = MutableLiveData<Person?>(null)
    val person: LiveData<Person?> = _person

    private val _isGuest = MutableLiveData(false)
    val isGuest: LiveData<Boolean> = _isGuest

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _event = SingleLiveEvent<MenuEvent>()
    val event: LiveData<MenuEvent> = _event

    /** Carga la persona actual y ajusta el estado guest/usuario. */
    fun loadCurrentPerson() {
        val token = sessionManager.getToken()
        if (token.isEmpty()) {
            _isGuest.value = true
            _event.value = MenuEvent.ShowLoginDialog
            return
        }

        viewModelScope.launch {
            _loading.value = true
            try {
                val user = appRepository.onGetProfile(token, 1)
                LogManager.d(TAG, "MenuVM: usuario cargado => $user")
                val person = Person(
                    name = user.name.orEmpty(),
                    cpf = user.cpf.orEmpty(),
                    email = user.email.orEmpty(),
                    phone = user.codeArea.orEmpty() + user.phone.orEmpty(),
                    codePlan = user.codePlan.orEmpty(),
                    descriptionPlan = user.descriptionPlan.orEmpty(),
                    photo = user.photo.orEmpty()
                )
                _person.value = person
                _isGuest.value = false
            } catch (e: Exception) {
                LogManager.e(TAG, "MenuVM: error al cargar persona => ${e.message}")
                _isGuest.value = true
                _event.value = MenuEvent.ShowLoginDialog
            } finally {
                _loading.value = false
            }
        }
    }

    fun onMenuClickedAsGuest() {
        _event.value = MenuEvent.ShowLoginDialog
    }

    fun onLogoutConfirmed() {
        val token = sessionManager.getToken()
        viewModelScope.launch {
            try {
                if (token.isNotEmpty()) {
                    appRepository.onDoLogoff(token)
                }
            } catch (e: Exception) {
                LogManager.e(TAG, "MenuVM: error en logoff => ${e.message}")
            } finally {
                sessionManager.clearSession()
                _event.value = MenuEvent.LoggedOut
            }
        }
    }

    private companion object {
        const val TAG = "MenuViewModel"
    }
}