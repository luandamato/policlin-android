package br.com.policlinsaude.ui.activities.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.policlinsaude.data.local.SessionManager
import br.com.policlinsaude.data.models.PresentationEstablishment
import br.com.policlinsaude.data.models.toPresentation
import br.com.policlinsaude.data.repositories.AppRepository
import br.com.policlinsaude.util.helpers.SingleLiveEvent
import kotlinx.coroutines.launch

/**
 * Eventos únicos da tela de favoritos.
 */
sealed interface FavoritesEvent {
    data class ShowError(val message: String) : FavoritesEvent
    data object ShowWithoutNetwork : FavoritesEvent
    data class ShowDetails(val establishment: PresentationEstablishment) : FavoritesEvent
}

/**
 * ViewModel da tela de favoritos (MVVM).
 *
 * Migrado de `_legacy/.../favorites/presenter/FavoritesPresenterImpl.kt`:
 * - carrega `MAPP_RetornaFavoritos` via [AppRepository.onGetFavorites] (suspend, já migrado);
 * - valida `msgInternal` (mesma regra do legado `validateMsgIsSuccess`);
 * - preserva o cache offline via [SessionManager] (legado: SharedPreferences "myPrefs/favoritesPref");
 * - expõe estado (loading/favorites) e eventos únicos para a UI reagir.
 *
 * Não referencia View/Activity/Fragment/Binding e não navega.
 */
class FavoritesViewModel(
    private val appRepository: AppRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _favorites = MutableLiveData<List<PresentationEstablishment>>(emptyList())
    val favorites: LiveData<List<PresentationEstablishment>> = _favorites

    private val _event = SingleLiveEvent<FavoritesEvent>()
    val event: LiveData<FavoritesEvent> = _event

    /**
     * Carrega os favoritos (mesma lógica do legado `FavoritesPresenterImpl.getFavorites`):
     * - sem conexão: emite evento de diálogo e renderiza o cache salvo, se existir;
     * - com conexão: chama a API, salva o cache e atualiza a lista.
     */
    fun loadFavorites(isOnline: Boolean) {
        if (!isOnline) {
            _event.value = FavoritesEvent.ShowWithoutNetwork
            val cached = sessionManager.getFavorites()
            if (cached.isNotEmpty()) {
                _favorites.value = cached
            }
            return
        }

        viewModelScope.launch {
            _loading.value = true
            try {
                val response = appRepository.onGetFavorites(sessionManager.getToken())

                if (response.msgInternal.isNullOrEmpty() || response.msgInternal != "OK") {
                    _event.value = FavoritesEvent.ShowError(response.msgExternal.orEmpty())
                } else {
                    val establishments = response.infs.orEmpty()
                        .mapNotNull { it.establishment?.toPresentation() }
                    sessionManager.saveFavorites(establishments)
                    _favorites.value = establishments
                }
            } catch (e: Exception) {
                _event.value = FavoritesEvent.ShowError(e.message.orEmpty())
            } finally {
                _loading.value = false
            }
        }
    }

    /** Click em um favorito (legado: onEstablishmentClicked) — a UI decide a navegação. */
    fun onEstablishmentClicked(establishmentIndex: Int) {
        val establishment = _favorites.value?.getOrNull(establishmentIndex) ?: return
        establishment.favorited = true
        _event.value = FavoritesEvent.ShowDetails(establishment)
    }
}