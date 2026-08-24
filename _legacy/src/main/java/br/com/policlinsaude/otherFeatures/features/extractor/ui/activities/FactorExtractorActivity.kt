package com.policlinsaude.newfeature.features.extractor.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import br.com.policlinsaude.R
import br.com.policlinsaude.databinding.ActivityFactorExtractorBinding
import br.com.policlinsaude.databinding.ActivityTicketsBinding
import com.policlinsaude.newfeature.features.extractor.ui.viewmodels.FactorExtractorViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.widget.TextView
class FactorExtractorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFactorExtractorBinding

    private val viewModel: FactorExtractorViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFactorExtractorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.factor_extractor_toolbar))

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        viewModel.isCoPartFm = intent.getBooleanExtra("CO_PART_FM", false)

        val config = AppBarConfiguration(navController.graph)

        setupActionBarWithNavController(navController, config)

        binding.factorExtractorToolbar.toolbar.setupWithNavController(navController)
        (binding.factorExtractorToolbar.toolbar.getChildAt(0) as TextView).textSize = 14f
    }

    fun showBackButton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.factorExtractorToolbar.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}