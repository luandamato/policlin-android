package br.com.policlinsaude.ui.fragments.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.policlinsaude.data.local.SessionManager
import br.com.policlinsaude.data.models.NotificationsModel
import br.com.policlinsaude.data.models.NotificationDeleteRequest
import br.com.policlinsaude.data.repositories.AppRepository
import br.com.policlinsaude.util.helpers.SingleLiveEvent
import kotlinx.coroutines.launch

/** Eventos únicos da tela de notificações. */
sealed interface NotificationEvent {
    data object Nothing : NotificationEvent
    data class ShowError(val message: String) : NotificationEvent
    data class NotificationsLoaded(val list: List<NotificationsModel>) : NotificationEvent
    data object DeleteSuccess : NotificationEvent
}

/**
 * ViewModel da tela de notificações.
 *
 * Migrado do legado `NotificationViewModel` (stack MVVM antigo `com.policlinsaude.newfeature`),
 * agora usando [AppRepository] + [SessionManager] e LiveData/SingleLiveEvent
 * (o legado expunha `ViewModelResponse<...>`; convertido para o padrão do novo app).
 */
class NotificationViewModel(
    private val appRepository: AppRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _event = SingleLiveEvent<NotificationEvent>()
    val event: LiveData<NotificationEvent> = _event

    val notificationsSelected: MutableLiveData<MutableList<NotificationsModel>> =
        MutableLiveData(arrayListOf())

    // =====================================================================
    // Seleção
    // =====================================================================
    fun addSelectedNotification(item: NotificationsModel) {
        notificationsSelected.value?.add(item)
    }

    fun removeSelectedNotification(item: NotificationsModel) {
        notificationsSelected.value?.remove(item)
    }

    fun addAllItems(items: List<NotificationsModel>) {
        notificationsSelected.value = items.toMutableList()
    }

    fun clearSelectedNotifications() {
        notificationsSelected.value = arrayListOf()
    }

    // =====================================================================
    // API
    // =====================================================================
    fun onGetNotifications() {
        val token = sessionManager.getToken()
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = appRepository.onGetNotifications(token)
                if (response.list.isNullOrEmpty()) {
                    _event.value = NotificationEvent.ShowError(
                        response.msgExterna.ifBlank { "Nenhuma notificação encontrada." }
                    )
                } else {
                    _event.value = NotificationEvent.NotificationsLoaded(response.list)
                }
            } catch (e: Exception) {
                _event.value = NotificationEvent.ShowError(
                    e.message?.takeIf { it.isNotBlank() } ?: "Ocorreu um erro inesperado."
                )
            } finally {
                _loading.value = false
            }
        }
    }

    fun onDeleteNotifications() {
        val token = sessionManager.getToken()
        viewModelScope.launch {
            _loading.value = true
            try {
                val body = NotificationDeleteRequest(
                    id = notificationsSelected.value?.joinToString(",") { it.id }.orEmpty()
                )
                val response = appRepository.onDeleteNotifications(token, body)

                if (response.codAcao == 1) {
                    notificationsSelected.value = arrayListOf()
                    _event.value = NotificationEvent.DeleteSuccess
                } else {
                    _event.value = NotificationEvent.ShowError(
                        response.msgExterna.ifBlank { "Não foi possível apagar as notificações." }
                    )
                }
            } catch (e: Exception) {
                _event.value = NotificationEvent.ShowError(
                    e.message?.takeIf { it.isNotBlank() } ?: "Ocorreu um erro inesperado."
                )
            } finally {
                _loading.value = false
            }
        }
    }
}