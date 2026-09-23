package br.com.policlinsaude.ui.fragments.perfil

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.policlinsaude.data.local.SessionManager
import br.com.policlinsaude.data.models.PresentationPerson
import br.com.policlinsaude.data.models.PresentationPlan
import br.com.policlinsaude.data.models.UserModel
import br.com.policlinsaude.data.repositories.AppRepository
import br.com.policlinsaude.util.helpers.InvalidData
import br.com.policlinsaude.util.helpers.SingleLiveEvent
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** Eventos da tela de perfil */
sealed interface PerfilEvent {
    data class ShowError(val message: String) : PerfilEvent
    data object ShowImagePickError : PerfilEvent
    data object AvatarChangedSuccess : PerfilEvent
}

/**
 * ViewModel da feature Perfil.
 *
 * Responsável por coordenar a obtenção dos dados do usuário logado
 * e a atualização do avatar, utilizando [AppRepository] e [SessionManager].
 */
class PerfilViewModel(
    private val appRepository: AppRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _person = MutableLiveData<PresentationPerson>()
    val person: LiveData<PresentationPerson> = _person

    private val _event = SingleLiveEvent<PerfilEvent>()
    val event: LiveData<PerfilEvent> = _event

    /**
     * Busca os dados do perfil no repositório.
     * Corresponde ao `presenter.getPerfil()` do legado.
     */
    fun getPerfil() {
        val token = sessionManager.getToken()
        if (token.isEmpty()) return

        viewModelScope.launch {
            _loading.value = true
            try {
                // Legado passava verificaAlteracao = 1
                val userModel = appRepository.onGetProfile(token, 1)
                _person.value = mapToPresentation(userModel)
            } catch (e: Exception) {
                _event.value = PerfilEvent.ShowError(e.message ?: "Erro ao carregar perfil")
            } finally {
                _loading.value = false
            }
        }
    }

    /**
     * Atualiza a foto de perfil.
     * Corresponde ao `presenter.onImagePicked(imageString)` do legado.
     */
    fun updateAvatar(imageString: String) {
        val token = sessionManager.getToken()
        if (token.isEmpty()) return

        viewModelScope.launch {
            _loading.value = true
            try {
                appRepository.onUpdateAvatar(token, imageString)
                _event.value = PerfilEvent.AvatarChangedSuccess
                getPerfil()
            } catch (e: Exception) {
                _event.value = PerfilEvent.ShowError(e.message ?: "Erro ao atualizar avatar")
            } finally {
                _loading.value = false
            }
        }
    }

    fun onImagePickError() {
        _event.value = PerfilEvent.ShowImagePickError
    }

    /**
     * Mapeia [UserModel] (API) para [PresentationPerson] (UI).
     * Mantém compatibilidade com o legado e com as extensões de Bitmap do novo app.
     */
    private fun mapToPresentation(user: UserModel): PresentationPerson {
        return PresentationPerson().apply {
            name = user.name.orEmpty()
            cpf = user.cpf.orEmpty()
            birthday = parseDate(user.birthday)
            phone = (user.codeArea.orEmpty()) + (user.phone.orEmpty())
            email = user.email.orEmpty()
            photo = user.photo.orEmpty()
            codePlan = user.codePlan.orEmpty()
            plan = PresentationPlan().apply {
                register = user.register.orEmpty()
                order = user.order.orEmpty()
                contract = user.contract.orEmpty()
            }
        }
    }

    private fun parseDate(dateString: String?): Date {
        if (dateString.isNullOrBlank()) return InvalidData.UNINITIALIZED.getDate()
        return try {
            // Tenta o formato completo vindo da API legado
            SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault()).parse(dateString)
                ?: SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dateString)
                ?: InvalidData.UNINITIALIZED.getDate()
        } catch (e: Exception) {
            InvalidData.UNINITIALIZED.getDate()
        }
    }
}
