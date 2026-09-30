package br.com.policlinsaude.ui.activities.ownNetwork

import br.com.policlinsaude.data.models.PresentationEstablishment

/**
 * Grupo de establecimientos de la Rede Propia — una página/tab por ciudad.
 *
 * Reemplaza al `Pair<String, List<PresentationEstablishment>>` del legado:
 * `key` es el identificador del grupo (cidCod) y `establishments` las sucursales.
 */
data class OwnNetworkGroup(
    val key: String,
    val establishments: MutableList<PresentationEstablishment>
)