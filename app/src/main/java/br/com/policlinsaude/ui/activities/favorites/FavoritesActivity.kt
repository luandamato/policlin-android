package br.com.policlinsaude.ui.activities.favorites

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.policlinsaude.R
import br.com.policlinsaude.data.models.PresentationEstablishment
import br.com.policlinsaude.databinding.ActivityFavoritesBinding
import br.com.policlinsaude.ui.activities.unitDetail.UnitDetailActivity
import br.com.policlinsaude.ui.dialogs.DialogHelper
import br.com.policlinsaude.ui.views.BaseActivity
import br.com.policlinsaude.util.helpers.ConnectivityHelper
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Tela de favoritos (MVVM).
 *
 * Migrado de `_legacy/.../favorites/view/FavoritesActivity.kt` (MVP → MVVM):
 * - lista de estabelecimentos favoritos agrupada por especialidade;
 * - loading, erro com "tentar novamente" e diálogo "sem conexão" (com cache offline);
 * - navegação na UI: detalhe do estabelecimento → UnitDetailActivity (destino já migrado).
 *
 * Fluxo: UI → FavoritesViewModel → AppRepository.onGetFavorites → AppService
 *        UI ←--- LiveData/Event ---- FavoritesViewModel
 */
class FavoritesActivity : BaseActivity(), FavoritesAdapter.OnItemClickListener {

    companion object {
        private const val FAVORITES_CALLER = "Favorites"

        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, FavoritesActivity::class.java))
        }
    }

    private lateinit var binding: ActivityFavoritesBinding

    private val viewModel: FavoritesViewModel by viewModel()

    private lateinit var adapter: FavoritesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar(binding.toolbar.toolbar)

        setupRecyclerView()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadFavorites(isOnline = ConnectivityHelper.isOnline(this))
    }

    override fun onItemClick(establishmentIndex: Int) {
        viewModel.onEstablishmentClicked(establishmentIndex)
    }

    // =====================================================================
    // RecyclerView
    // =====================================================================
    private fun setupRecyclerView() {
        adapter = FavoritesAdapter(this)
        binding.favoritesRecyclerView.adapter = adapter
        val layoutManager = LinearLayoutManager(this)
        binding.favoritesRecyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                layoutManager.orientation
            )
        )
        binding.favoritesRecyclerView.layoutManager = layoutManager
    }

    // =====================================================================
    // Observers (estado/eventos do ViewModel)
    // =====================================================================
    private fun observeViewModel() {
        viewModel.loading.observe(this) { loading ->
            binding.loadingView.root.visibility = if (loading) View.VISIBLE else View.GONE
        }

        viewModel.favorites.observe(this) { favorites ->
            adapter.setEstablishemtns(ArrayList(favorites))
        }

        viewModel.event.observe(this) { event ->
            when (event) {
                is FavoritesEvent.ShowError -> showDialogError(event.message)
                FavoritesEvent.ShowWithoutNetwork -> showWithoutNetworkDialog()
                is FavoritesEvent.ShowDetails -> navigateToDetails(event.establishment)
            }
        }
    }

    // =====================================================================
    // Helpers visuais (loading, dialogs)
    // =====================================================================
    private fun showDialogError(message: String) {
        showDialogTryAgain(
            listenerPositiveButton = {
                viewModel.loadFavorites(isOnline = ConnectivityHelper.isOnline(this))
            },
            message = message
        )
    }

    private fun showWithoutNetworkDialog() {
        DialogHelper.showDialog(
            this,
            getString(R.string.title_no_internet_connection),
            getString(R.string.text_no_internet_favorites),
            getString(R.string.text_ok),
            null
        )
    }

    // =====================================================================
    // Navegação (na UI) — detalhe já migrado como UnitDetailActivity
    // =====================================================================
    private fun navigateToDetails(establishment: PresentationEstablishment) {
        UnitDetailActivity.start(this, establishment, FAVORITES_CALLER)
    }
}