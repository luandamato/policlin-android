package com.policlinsaude.newfeature.features.notifications.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.policlinsaude.newfeature.R
import com.policlinsaude.newfeature.databinding.ActivityFactorExtractorBinding
import com.policlinsaude.newfeature.databinding.ActivityNotificationBinding
import android.view.Menu
import android.view.MenuItem


class NotificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.notification_toolbar))

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController


        val config = AppBarConfiguration(navController.graph)

        setupActionBarWithNavController(navController, config)
    }

    fun showBackButton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.notificationToolbar.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    fun showButtonCancel(isShow: Boolean, listener: () -> Unit = {}) {
        binding.notificationToolbar.toolbar.menu.clear()
        if (isShow)
            binding.notificationToolbar.toolbar.menu.apply {
                add(0, 0, 0, "Cancelar").apply {
                    setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
                }.setOnMenuItemClickListener {
                    listener.invoke()
                    true
                }

            }
    }

    fun showButtonEdit(isShow: Boolean, listener: () -> Unit = {}) {
        binding.notificationToolbar.toolbar.menu.clear()
        if (isShow)
            binding.notificationToolbar.toolbar.menu.apply {
                add(0, 0, 0, "Editar").apply {
                    setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
                }.setOnMenuItemClickListener {
                    listener.invoke()
                    true
                }
            }
    }


}