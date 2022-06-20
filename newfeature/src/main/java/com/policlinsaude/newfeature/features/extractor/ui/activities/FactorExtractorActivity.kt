package com.policlinsaude.newfeature.features.extractor.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.policlinsaude.newfeature.R
import com.policlinsaude.newfeature.databinding.ActivityFactorExtractorBinding
import com.policlinsaude.newfeature.databinding.ActivityTicketsBinding

class FactorExtractorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFactorExtractorBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFactorExtractorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.factor_extractor_toolbar))

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val config = AppBarConfiguration(navController.graph)

        setupActionBarWithNavController(navController, config)

        binding.factorExtractorToolbar.toolbar.setupWithNavController(navController)
    }

    fun showBackButton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.factorExtractorToolbar.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}