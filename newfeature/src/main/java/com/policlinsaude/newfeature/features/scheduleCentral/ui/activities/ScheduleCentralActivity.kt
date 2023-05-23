package com.policlinsaude.newfeature.features.scheduleCentral.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.policlinsaude.newfeature.R
import com.policlinsaude.newfeature.databinding.ActivityScheduleCentralBinding

class ScheduleCentralActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScheduleCentralBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleCentralBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.central_toolbar))

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_schedule_central) as NavHostFragment
        val navController = navHostFragment.navController

        val config = AppBarConfiguration(navController.graph)

        setupActionBarWithNavController(navController, config)

        (binding.centralToolbar.toolbar.getChildAt(0) as TextView).textSize = 16f
    }

    fun showBackButton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.centralToolbar.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}