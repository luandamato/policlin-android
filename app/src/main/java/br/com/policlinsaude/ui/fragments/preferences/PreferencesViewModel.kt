package br.com.policlinsaude.ui.fragments.preferences

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.policlinsaude.data.local.SessionManager
import br.com.policlinsaude.data.repositories.AppRepository
import br.com.policlinsaude.util.helpers.SingleLiveEvent
import kotlinx.coroutines.launch

/** Eventos da tela de preferências */
sealed interface PreferencesEvent {
    data class ShowError(val message: String) : PreferencesEvent
    data object ShowLogoffConfirmation : PreferencesEvent
    data object LogoffSuccess : PreferencesEvent
    data object AskForPermissions : PreferencesEvent
}

/**
 * ViewModel da tela de Preferências.
 * Coordena o estado dos switches (Notificação e Localização) e o processo de logout.
 */
class PreferencesViewModel(
    private val appRepository: AppRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _notificationEnabled = MutableLiveData<Boolean>()
    val notificationEnabled: LiveData<Boolean> = _notificationEnabled

    private val _locationEnabled = MutableLiveData<Boolean>()
    val locationEnabled: LiveData<Boolean> = _locationEnabled

    private val _event = SingleLiveEvent<PreferencesEvent>()
    val event: LiveData<PreferencesEvent> = _event

    private var pendingLocationState: Boolean = false

    companion object {
        private const val KEY_NOTIFICATION = "notification"
        private const val KEY_LOCATION = "location"
    }

    init {
        loadPreferences()
    }

    private fun loadPreferences() {
        _notificationEnabled.value = sessionManager.getBoolean(KEY_NOTIFICATION, true)
        _locationEnabled.value = sessionManager.getBoolean(KEY_LOCATION, false)
    }

    fun onNotificationStateChanged(checked: Boolean) {
        sessionManager.putBoolean(KEY_NOTIFICATION, checked)
        _notificationEnabled.value = checked
        // O legado chamava o endpoint setNotificationPreference se necessário. 
        // No AppRepository atual não temos esse endpoint específico mapeado além do logoff/perfil.
        // Se houver necessidade de persistir no server, chamar appRepository aqui.
    }

    fun onLocationStateChanged(checked: Boolean) {
        pendingLocationState = checked
        if (checked) {
            _event.value = PreferencesEvent.AskForPermissions
        } else {
            updateLocationPreference(false)
        }
    }

    fun onPermissionsGranted() {
        updateLocationPreference(pendingLocationState)
    }

    fun onPermissionsDenied() {
        _locationEnabled.value = false
        updateLocationPreference(false)
    }

    private fun updateLocationPreference(enabled: Boolean) {
        sessionManager.putBoolean(KEY_LOCATION, enabled)
        _locationEnabled.value = enabled
    }

    fun onLogoffClicked() {
        _event.value = PreferencesEvent.ShowLogoffConfirmation
    }

    fun onLogoutConfirmed() {
        val token = sessionManager.getToken()
        viewModelScope.launch {
            _loading.value = true
            try {
                // Tenta chamar o endpoint de logoff se houver token
                if (token.isNotEmpty()) {
                    appRepository.onDoLogoff(token)
                }
                sessionManager.clearSession()
                _event.value = PreferencesEvent.LogoffSuccess
            } catch (e: Exception) {
                // Mesmo com erro na API, limpamos a sessão local conforme comportamento esperado
                sessionManager.clearSession()
                _event.value = PreferencesEvent.LogoffSuccess
            } finally {
                _loading.value = false
            }
        }
    }
}
