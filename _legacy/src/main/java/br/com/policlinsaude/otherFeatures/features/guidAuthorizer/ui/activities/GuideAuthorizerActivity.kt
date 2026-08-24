package com.policlinsaude.newfeature.features.guidAuthorizer.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import br.com.policlinsaude.R
import br.com.policlinsaude.databinding.ActivityGuideAuthorizerBinding
import com.policlinsaude.newfeature.features.guidAuthorizer.ui.viewmodels.GuideAuthorizerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class GuideAuthorizerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGuideAuthorizerBinding
    private val viewModel: GuideAuthorizerViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGuideAuthorizerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.guide_authorizer_toolbar))

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        viewModel.isFromActivity = true

        val config = AppBarConfiguration(navController.graph)

        setupActionBarWithNavController(navController, config)

        binding.guideAuthorizerToolbar.toolbar.setupWithNavController(navController)
        (binding.guideAuthorizerToolbar.toolbar.getChildAt(0) as TextView).textSize = 14f
    }

    fun showBackButton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.guideAuthorizerToolbar.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    fun showBackButton(listener: () -> Unit) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.guideAuthorizerToolbar.toolbar.setNavigationOnClickListener {
            listener.invoke()
            onBackPressed()
        }
    }

    fun showButtonCancel(isShow: Boolean, listener: () -> Unit = {}) {
        binding.guideAuthorizerToolbar.toolbar.menu.clear()
        if (isShow)
            binding.guideAuthorizerToolbar.toolbar.menu.apply {
                add(0, 0, 0, "Cancelar").apply {
                    setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
                }.setOnMenuItemClickListener {
                    listener.invoke()
                    true
                }
            }
    }

    fun showButtonEdit(isShow: Boolean, listener: () -> Unit = {}) {
        binding.guideAuthorizerToolbar.toolbar.menu.clear()
        if (isShow)
            binding.guideAuthorizerToolbar.toolbar.menu.apply {
                add(0, 0, 0, "Editar").apply {
                    setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
                }.setOnMenuItemClickListener {
                    listener.invoke()
                    true
                }
            }
    }
}