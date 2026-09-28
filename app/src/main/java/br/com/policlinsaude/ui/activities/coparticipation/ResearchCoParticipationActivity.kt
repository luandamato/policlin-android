package br.com.policlinsaude.ui.activities.coparticipation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.policlinsaude.R
import br.com.policlinsaude.data.models.CoParticipationItemsDetails
import br.com.policlinsaude.databinding.ActivityResearchCoParticipationBinding
import br.com.policlinsaude.ui.fragments.coparticipation.CoParticipationViewModel
import br.com.policlinsaude.ui.fragments.coparticipation.ResearchCoParticipationFiltersFragment
import br.com.policlinsaude.ui.fragments.coparticipation.ResearchCoParticipationItemsFragment
import br.com.policlinsaude.ui.views.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Pesquisa de Valores de Coparticipação (MVVM).
 *
 * Migrado de `_legacy/.../otherFeatures/features/coparticipation/ui/activities/ResearchCoParticipationActivity.kt`
 * (stack `com.policlinsaude.newfeature`). No legado a Activity usava `NavHostFragment` +
 * Navigation Component; agora hospeda os fragments (Filtros → Itens) em um container
 * simples com toolbar própria (mesmo padrão da `NotificationActivity` migrada).
 *
 * Fluxo novo: UI → `CoParticipationViewModel` → `AppRepository` → `AppService`.
 */
class ResearchCoParticipationActivity : BaseActivity() {

    companion object {
        private const val EXTRA_CO_PART_FM = "CO_PART_FM"

        fun start(activity: Activity, isCoPartFm: Boolean) {
            val intent = Intent(activity, ResearchCoParticipationActivity::class.java)
            intent.putExtra(EXTRA_CO_PART_FM, isCoPartFm)
            activity.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityResearchCoParticipationBinding

    val viewModel: CoParticipationViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResearchCoParticipationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.researchCoParticipationToolbar.toolbar)
        supportActionBar?.title = getString(R.string.title_research_co_participation)

        viewModel.isCoPartFm = intent.getBooleanExtra(EXTRA_CO_PART_FM, false)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ResearchCoParticipationFiltersFragment.newInstance())
                .commit()
        }
    }

    fun showBackButton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.researchCoParticipationToolbar.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    fun toolbar() = binding.researchCoParticipationToolbar.toolbar

    /** Navegação (UI): Filtros → Itens após resultado da pesquisa. */
    fun showItems(details: ArrayList<CoParticipationItemsDetails>) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, ResearchCoParticipationItemsFragment.newInstance(details))
            .addToBackStack(null)
            .commit()
    }
}
