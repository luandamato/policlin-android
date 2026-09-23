package br.com.policlinsaude.ui.activity_healthInsurancePhoto

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.policlinsaude.data.local.SessionManager
import br.com.policlinsaude.data.models.JsonHealthInsurancePhotoResponse
import br.com.policlinsaude.data.repositories.AppRepository
import br.com.policlinsaude.domain.models.HealthInsurancePhoto
import br.com.policlinsaude.domain.models.HealthInsurancePhotoList
import br.com.policlinsaude.util.helpers.SingleLiveEvent
import kotlinx.coroutines.launch

/** Eventos únicos da tela de carteirinha. */
sealed interface HealthInsurancePhotoEvent {
    data class ShowImage(val photoList: HealthInsurancePhotoList) : HealthInsurancePhotoEvent
    data class ShowError(val message: String) : HealthInsurancePhotoEvent
    data class ShowWithoutNetwork(val photoList: HealthInsurancePhotoList?) : HealthInsurancePhotoEvent
}

/**
 * ViewModel da carteirinha (frente/verso).
 *
 * Migrado de `_legacy/.../healthInsurancePhoto/presenter/HealthInsurancePhotoPresenterImpl.kt`.
 * O cache (SharedPreferences via [SessionManager]) preserva o comportamento do legado:
 * mostra as fotos guardadas quando não há rede.
 *
 * Fluxo: UI → ViewModel → AppRepository.onGetHealthInsurancePhoto → AppService.
 */
class HealthInsurancePhotoViewModel(
    private val appRepository: AppRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _event = SingleLiveEvent<HealthInsurancePhotoEvent>()
    val event: LiveData<HealthInsurancePhotoEvent> = _event

    /**
     * Carrega a carteirinha.
     * @param isOnline indica conectividade (avaliada na UI para manter o ViewModel
     *                 livre de referência a Context).
     */
    fun getImage(isOnline: Boolean) {
        if (!isOnline) {
            _loading.value = false
            _event.value = HealthInsurancePhotoEvent.ShowWithoutNetwork(loadFromCache())
            return
        }

        val token = sessionManager.getToken()
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = appRepository.onGetHealthInsurancePhoto(token, checkUpdated = 0)
                val photoList = response.toDomain()
                if (photoList.listaimgFrente.isNullOrEmpty() || photoList.imgVerso.isNullOrEmpty()) {
                    _event.value = HealthInsurancePhotoEvent.ShowError(
                        photoList.msgExterna?.takeIf { it.isNotBlank() } ?: "Não foi possível carregar a carteirinha."
                    )
                } else {
                    sessionManager.saveCarteirinha(photoList)
                    _event.value = HealthInsurancePhotoEvent.ShowImage(photoList)
                }
            } catch (e: Exception) {
                _event.value = HealthInsurancePhotoEvent.ShowError(
                    e.message?.takeIf { it.isNotBlank() } ?: "Ocorreu um erro inesperado."
                )
            } finally {
                _loading.value = false
            }
        }
    }

    // =====================================================================
    // Helpers
    // =====================================================================
    private fun loadFromCache(): HealthInsurancePhotoList? {
        val photos = sessionManager.getHealthInsurancePhotos()
        val verso = sessionManager.getHealthInsuranceVerso()
        return if (photos.isEmpty() && verso.isEmpty()) null
        else HealthInsurancePhotoList(
            listaimgFrente = photos,
            imgVerso = verso.ifBlank { null }
        )
    }

    private fun JsonHealthInsurancePhotoResponse.toDomain(): HealthInsurancePhotoList =
        HealthInsurancePhotoList(
            listaimgFrente = listImages.orEmpty().map { it.toDomain() },
            imgVerso = imgVerso,
            codAcao = codAcao,
            msgInterna = msgInternal,
            msgExterna = msgExternal
        )

    private fun br.com.policlinsaude.data.models.JsonHealthInsurancePhotoListResponse.toDomain() =
        HealthInsurancePhoto(imgFrente = imageFront, titular = titular, ordem = ordem)
}