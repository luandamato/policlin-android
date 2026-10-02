package br.com.policlinsaude.ui.activities.ownNetwork

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import br.com.policlinsaude.R
import br.com.policlinsaude.databinding.ActivityOwnNetworkBinding
import br.com.policlinsaude.ui.activities.unitDetail.UnitDetailActivity
import br.com.policlinsaude.ui.views.BaseActivity
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Pantalla Rede Propia (ownNetwork) — MVVM.
 *
 * Migrado de `_legacy/.../ownNetwork/view/OwnNetworkActivity.kt` (MVP → MVVM):
 * - tabs por ciudad (páginas del ViewPager2) con misma orden del legado;
 * - loading, error con reintentar y acción de mapa en la toolbar;
 * - navegación en la UI: destinos aún no migrados (detalles del
 *   establecimiento y mapa) muestran Toast "Em construção".
 *
 * UI → OwnNetworkViewModel → AppRepository.onGetOwnNetwork → AppService
 * UI ←--- LiveData/Event ---- OwnNetworkViewModel
 */
class OwnNetworkActivity : BaseActivity() {

    companion object {
        private const val OWN_NETWORK_CALLER = "OwnNetwork"

        fun start(activity: android.app.Activity) {
            val intent = Intent(activity, OwnNetworkActivity::class.java)
            activity.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityOwnNetworkBinding

    private val viewModel: OwnNetworkViewModel by viewModel()

    private lateinit var pageAdapter: OwnNetworkPageAdapter
    private lateinit var tabAdapter: OwnNetworkTabAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOwnNetworkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar(binding.toolbar.toolbar)
        bindTabs()
        observeViewModel()

        viewModel.loadOwnNetwork()
    }

    // =====================================================================
    // Menú de la toolbar (mapa) — mismo item del legado
    // =====================================================================
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_medical_guide_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        item.let {
            when (item.itemId) {
                R.id.action_map -> {
                    viewModel.onMapClicked(binding.viewPager.currentItem)
                    return true
                }
                else -> {
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // =====================================================================
    // Tabs (ciudades) — RecyclerView horizontal
    // =====================================================================
    private fun bindTabs() {
        tabAdapter = OwnNetworkTabAdapter(this) { index -> onTabClicked(index) }
        binding.tabsRecyclerView.apply {
            adapter = this@OwnNetworkActivity.tabAdapter
            layoutManager = FlexboxLayoutManager(this@OwnNetworkActivity).apply {
                flexDirection = FlexDirection.ROW
                flexWrap = FlexWrap.NOWRAP
                justifyContent = JustifyContent.FLEX_START
            }
        }
    }

    private fun onTabClicked(index: Int) {
        binding.viewPager.currentItem = index
        tabAdapter.setSelected(index)
    }

    private fun updateTabs() {
        val groups = viewModel.groups.value ?: emptyList()
        val tabs = groups.map { group ->
            OwnNetworkTabAdapter.OwnNetworkTab(tabTitle(group))
        }
        tabAdapter.update(tabs, safeTabIndex(binding.viewPager.currentItem, tabs.size))
    }

    private fun tabTitle(group: OwnNetworkGroup): String {
        return group.establishments.getOrNull(0)?.city ?: group.key
    }

    private fun safeTabIndex(current: Int, size: Int): Int {
        return if (size == 0) 0 else minOf(maxOf(current, 0), size - 1)
    }

    // =====================================================================
    // Observers (estado/eventos do ViewModel)
    // =====================================================================
    private fun observeViewModel() {
        viewModel.loading.observe(this) { isLoading ->
            binding.loadingView.root.visibility =
                if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.groups.observe(this) { groups ->
            pageAdapter = OwnNetworkPageAdapter(
                this,
                groups,
                viewModel.qualifications.value ?: emptyList()
            ) { establishment ->
                viewModel.onItemClick(establishment)
            }
            binding.viewPager.adapter = pageAdapter
            binding.viewPager.currentItem = 0
            updateTabs()
        }

        viewModel.event.observe(this) { event ->
            when (event) {
                is OwnNetworkEvent.ShowError -> showDialogError(event.message)
                is OwnNetworkEvent.ShowDetails -> navigateToDetails(event.establishment)
                is OwnNetworkEvent.ShowMap -> notMigrated()
            }
        }
    }

    // =====================================================================
    // Helpers visuales (loading, dialogs, toasts)
    // =====================================================================
    private fun showDialogError(message: String) {
        val listener = { viewModel.loadOwnNetwork() }
        showDialogTryAgain(listenerPositiveButton = listener, message = message)
    }

    /** Navegación na UI: detalle → UnitDetailActivity en modo Rede Propia (favoritos habilitados). */
    private fun navigateToDetails(establishment: br.com.policlinsaude.data.models.PresentationEstablishment) {
        UnitDetailActivity.start(this, establishment, OWN_NETWORK_CALLER)
    }

    private fun notMigrated() {
        Toast.makeText(this, "Em construção", Toast.LENGTH_SHORT).show()
    }
}