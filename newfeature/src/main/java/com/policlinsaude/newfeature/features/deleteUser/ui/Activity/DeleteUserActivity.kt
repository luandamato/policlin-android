package com.policlinsaude.newfeature.features.deleteUser.ui.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.policlinsaude.newfeature.R
import com.policlinsaude.newfeature.databinding.ActivityDeleteUserBinding

class DeleteUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeleteUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.delete_user_toolbar))

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_delete_user) as NavHostFragment
        val navController = navHostFragment.navController

        val config = AppBarConfiguration(navController.graph)

        setupActionBarWithNavController(navController, config)

//        binding.centralToolbar.toolbar.setupWithNavController(navController)
//        (binding.deleteUserToolbar.toolbar.getChildAt(0) as TextView).textSize = 16f
    }

    fun showBackButton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.deleteUserToolbar.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}