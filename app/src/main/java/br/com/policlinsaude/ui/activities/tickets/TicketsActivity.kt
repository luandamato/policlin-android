package br.com.policlinsaude.ui.activities.tickets

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.policlinsaude.R
import br.com.policlinsaude.data.models.TicketDetail
import br.com.policlinsaude.databinding.ActivityTicketsBinding
import br.com.policlinsaude.ui.fragments.tickets.TicketDetailFragment
import br.com.policlinsaude.ui.fragments.tickets.TicketsFragment

/**
 * Tela de 2ª via de boletos (MVVM).
 *
 * Migrado do legado `TicketsActivity` — antes usava NavHostFragment +
 * Navigation Component (`tickets_nav_graph`); agora hospeda [TicketsFragment]
 * e [TicketDetailFragment] em um container simples (mesmo padrão de
 * NotificationActivity/TokenActivity) com toolbar própria.
 *
 * Fluxo novo: UI → TicketViewModel → AppRepository (apiBoletos).
 * A navegação para o detalhe é feita na UI via [showDetail].
 */
class TicketsActivity : AppCompatActivity() {

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, TicketsActivity::class.java)
            activity.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityTicketsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTicketsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.ticketToolbar.toolbar)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, TicketsFragment.newInstance())
                .commit()
        }
    }

    fun showBackButton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.ticketToolbar.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    fun showDetail(detail: TicketDetail) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, TicketDetailFragment.newInstance(detail))
            .addToBackStack(null)
            .commit()
    }
}