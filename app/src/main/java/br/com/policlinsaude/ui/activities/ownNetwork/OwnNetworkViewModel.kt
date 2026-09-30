package br.com.policlinsaude.ui.activities.ownNetwork

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.policlinsaude.data.local.SessionManager
import br.com.policlinsaude.data.models.PresentationEstablishment
import br.com.policlinsaude.data.models.PresentationQualification
import br.com.policlinsaude.data.models.toPresentation
import br.com.policlinsaude.data.repositories.AppRepository
import br.com.policlinsaude.util.helpers.SingleLiveEvent
import kotlinx.coroutines.launch

/**
 * Eventos únicos de la pantalla Rede Propia.
 */
sealed interface OwnNetworkEvent {
    data class ShowError(val message: String) : OwnNetworkEvent
    data class ShowDetails(val establishment: PresentationEstablishment) : OwnNetworkEvent
    data class ShowMap(val establishments: List<PresentationEstablishment>) : OwnNetworkEvent
}

/**
 * ViewModel de Rede Propia (ownNetwork).
 *
 * Migrado de `_legacy/.../ownNetwork/presenter/OwnNetworkPresenterImpl.kt`:
 * - carga `MAPP_RetornaRedePropria` vía `AppRepository.onGetOwnNetwork(token)`;
 * - agrupa establecimientos por `cidCod` (misma lógica del `JsonOwnNetworkResponseMapper`);
 * - reordena moviendo el grupo "São José dos Campos" al inicio (misma regla del legado);
 * - expone estado (loading/groups/qualifications) y eventos de navegación para la UI.
 *
 * No referencia View/Activity/Fragment/Binding y no navega.
 */
class OwnNetworkViewModel(
    private val appRepository: AppRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _groups = MutableLiveData<List<OwnNetworkGroup>>(emptyList())
    val groups: LiveData<List<OwnNetworkGroup>> = _groups

    private val _qualifications = MutableLiveData<List<PresentationQualification>>(emptyList())
    val qualifications: LiveData<List<PresentationQualification>> = _qualifications

    private val _event = SingleLiveEvent<OwnNetworkEvent>()
    val event: LiveData<OwnNetworkEvent> = _event

    /** Carga la rede propia (llamado al abrir la pantalla y al reintentar). */
    fun loadOwnNetwork() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val token = sessionManager.getToken()
                val response = appRepository.onGetOwnNetwork(if (token.isEmpty()) null else token)

                _qualifications.value = response.qualifications?.map { it.toPresentation() } ?: emptyList()
                _groups.value = reorderSaoJose(
                    groupByCidCod(response.establishments?.map { it.toPresentation() } ?: emptyList())
                )
            } catch (e: Exception) {
                _event.value = OwnNetworkEvent.ShowError(e.message ?: "")
            } finally {
                _loading.value = false
            }
        }
    }

    /** Click en un establecimiento → la UI decide la navegación (destino aún legado → "Em construção"). */
    fun onItemClick(establishment: PresentationEstablishment) {
        _event.value = OwnNetworkEvent.ShowDetails(establishment)
    }

    /** Click en el mapa del tab seleccionado → la UI decide la navegación (destino aún legado → "Em construção"). */
    fun onMapClicked(tabIndex: Int) {
        val group = _groups.value?.getOrNull(tabIndex) ?: return
        _event.value = OwnNetworkEvent.ShowMap(group.establishments.toList())
    }

    // =====================================================================
    // Agrupación por cidCod (misma lógica del legado JsonOwnNetworkResponseMapper)
    // =====================================================================
    private fun groupByCidCod(establishments: List<PresentationEstablishment>): List<OwnNetworkGroup> {
        val groups = mutableListOf<OwnNetworkGroup>()
        establishments.forEach { establishment ->
            if (groups.isEmpty()) {
                groups.add(OwnNetworkGroup(establishment.cidCod, mutableListOf(establishment)))
                return@forEach
            }
            groups.forEachIndexed { index, group ->
                if (group.key == establishment.cidCod) {
                    group.establishments.add(establishment)
                    return@forEach
                } else if ((index + 1) == groups.count()) {
                    groups.add(OwnNetworkGroup(establishment.cidCod, mutableListOf(establishment)))
                }
            }
        }
        return groups
    }

    // =====================================================================
    // Reorder "São José dos Campos" al inicio (misma regla del legado)
    // =====================================================================
    private fun reorderSaoJose(groups: List<OwnNetworkGroup>): List<OwnNetworkGroup> {
        val list = groups.toMutableList()
        for (i in 0 until list.size) {
            if (list[i].key.equals("São José dos Campos", true)) {
                list.add(0, list.removeAt(i))
                break
            }
        }
        return list.toList()
    }
}