package com.policlinsaude.newfeature.features.coparticipation.ui.activities

import android.os.Bundle
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.marginStart
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.policlinsaude.newfeature.R
import com.policlinsaude.newfeature.databinding.ActivityResearchCoParticipationBinding
import com.policlinsaude.newfeature.features.coparticipation.ui.viewmodels.CoParticipationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ResearchCoParticipationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResearchCoParticipationBinding
    private val viewModel: CoParticipationViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResearchCoParticipationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.research_co_participation_toolbar))

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_research_co_participation_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        viewModel.isCoPartFm = intent.getBooleanExtra("CO_PART_FM", false)

        val config = AppBarConfiguration(navController.graph)

        setupActionBarWithNavController(navController, config)

        binding.researchCoParticipationToolbar.toolbar.setupWithNavController(navController)
        binding.researchCoParticipationToolbar.toolbar.setTitleMargin(10, 0,10,0)
        (binding.researchCoParticipationToolbar.toolbar.getChildAt(0) as TextView).apply {
            val widthDp = resources.displayMetrics.density

            textSize = if(widthDp < 3) {
                14f
            } else {
                16f
            }
        }
    }

    fun showBackButton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.researchCoParticipationToolbar.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    fun toolbar() =  binding.researchCoParticipationToolbar.toolbar
}