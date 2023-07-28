package com.policlinsaude.newfeature.features.Token.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.policlinsaude.newfeature.R
import com.policlinsaude.newfeature.databinding.ActivityTokenBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class TokenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTokenBinding
    private val viewModel: TokenViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTokenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.token_toolbar))

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_token) as NavHostFragment
        val navController = navHostFragment.navController

        val config = AppBarConfiguration(navController.graph)

        setupActionBarWithNavController(navController, config)

//        binding.centralToolbar.toolbar.setupWithNavController(navController)
        (binding.tokenToolbar.toolbar.getChildAt(0) as TextView).textSize = 16f
    }

    fun showBackButton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.tokenToolbar.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}