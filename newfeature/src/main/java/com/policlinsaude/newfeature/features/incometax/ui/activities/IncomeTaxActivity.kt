package com.policlinsaude.newfeature.features.incometax.ui.activities

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.policlinsaude.newfeature.R
import com.policlinsaude.newfeature.databinding.ActivityIncomeTaxBinding

class IncomeTaxActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIncomeTaxBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIncomeTaxBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.ir_toolbar))

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_income_tax) as NavHostFragment
        val navController = navHostFragment.navController

        val config = AppBarConfiguration(navController.graph)

        setupActionBarWithNavController(navController, config)

        binding.irToolbar.toolbar.setupWithNavController(navController)
        (binding.irToolbar.toolbar.getChildAt(0) as TextView).textSize = 16f
    }

    fun showBackButton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.irToolbar.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

}