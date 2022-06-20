package com.policlinsaude.newfeature.features.coparticipation.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.policlinsaude.newfeature.R
import com.policlinsaude.newfeature.databinding.ActivityResearchCoParticipationBinding

class ResearchCoParticipationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResearchCoParticipationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResearchCoParticipationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.research_co_participation_toolbar))

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_research_co_participation_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val config = AppBarConfiguration(navController.graph)

        setupActionBarWithNavController(navController, config)

        binding.researchCoParticipationToolbar.toolbar.setupWithNavController(navController)
    }

    fun showBackButton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.researchCoParticipationToolbar.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}