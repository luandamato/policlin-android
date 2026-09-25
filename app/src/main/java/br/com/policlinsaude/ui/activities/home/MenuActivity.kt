package br.com.policlinsaude.ui.activities.home

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import br.com.policlinsaude.R
import br.com.policlinsaude.databinding.ActivityHomeBinding
import br.com.policlinsaude.ui.activities.home.MenuAdapter.OnMenuItemClickListener
import br.com.policlinsaude.ui.dialogs.DialogHelper
import br.com.policlinsaude.ui.fragments.home.HomeFragment
import br.com.policlinsaude.ui.activities.login.LoginActivity
import br.com.policlinsaude.ui.fragments.informations.InformationsFragment
import br.com.policlinsaude.ui.fragments.links.LinksFragment
import br.com.policlinsaude.ui.fragments.perfil.PerfilFragment
import br.com.policlinsaude.ui.fragments.preferences.PreferencesFragment
import br.com.policlinsaude.ui.fragments.units.UnitsFragment
import br.com.policlinsaude.ui.views.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Shell principal (home): drawer + barra superior.
 * Host de [HomeFragment] dentro de `content_layout`.
 * Migrado de `_legacy/.../home/view/MenuActivity.kt` (MVP → MVVM).
 */
class MenuActivity : BaseActivity(), OnMenuItemClickListener {

    companion object {
        fun start(activity: android.app.Activity) {
            val intent = android.content.Intent(activity, MenuActivity::class.java)
            intent.flags = android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP or android.content.Intent.FLAG_ACTIVITY_NEW_TASK
            activity.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityHomeBinding
    private lateinit var toggle: ActionBarDrawerToggle

    private val viewModel: MenuViewModel by viewModel()

    private var isGuest = false

    fun isGuestMode(): Boolean = isGuest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDrawer()
        setupListeners()
        observeViewModel()

        viewModel.loadCurrentPerson()
        showHomeFragment()
    }

    // =====================================================================
    // Drawer / toolbar
    // =====================================================================
    fun setupFragmentToolbar(toolbar: Toolbar?, @StringRes title: Int?) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = if (title == null) null else getString(title)

        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        toggle.apply {
            isDrawerIndicatorEnabled = true
            isDrawerSlideAnimationEnabled = true
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        toggle.drawerArrowDrawable.color = ContextCompat.getColor(this, R.color.white)
    }

    private fun setupDrawer() {
        // Toolbar: proviene del app_bar_home incluido en HomeFragment.
        setupFragmentToolbar(null, null)
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) return true
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    // =====================================================================
    // Listeners drawer
    // =====================================================================
    private fun setupListeners() {
        val menuItems = arrayOf(
            MenuOptionEnum.HOME,
            MenuOptionEnum.PROFILE,
            MenuOptionEnum.PREFERENCES,
            MenuOptionEnum.INFORMATION,
            MenuOptionEnum.UNITIES,
            MenuOptionEnum.LINKS,
            MenuOptionEnum.CIB,
            MenuOptionEnum.LOGOUT
        )
        val adapter = MenuAdapter(this, R.layout.list_item_menu, menuItems, this)
        binding.menuItemsListView.adapter = adapter

        binding.sair.setOnClickListener {
            DialogHelper.showDialog(
                this,
                getString(R.string.title_logoff),
                getString(R.string.text_logoff_confirmation),
                getString(R.string.global_yes),
                getString(R.string.action_cancel),
                listenerPositiveButton = { viewModel.onLogoutConfirmed() }
            )
        }
    }

    // =====================================================================
    // Observers ViewModel
    // =====================================================================
    private fun observeViewModel() {
        viewModel.person.observe(this) { person ->
            person?.let {
                binding.navHeader.nameTextView.text = it.name
                binding.navHeader.planTextView.text = it.descriptionPlan
            }
        }

        viewModel.isGuest.observe(this) { guest ->
            isGuest = guest
        }

        viewModel.event.observe(this) { event ->
            when (event) {
                is MenuEvent.ShowLoginDialog -> showLoginDialog()
                is MenuEvent.LoggedOut -> navigateToLogin()
                is MenuEvent.ShowError -> DialogHelper.showErrorDialog(this, event.message)
                is MenuEvent.ShowUpdateApp -> DialogHelper.showUpdateDialog(this, event.message)
            }
        }
    }

    // =====================================================================
    // Navegación (UI)
    // =====================================================================
    override fun onClick(menuItem: MenuOptionEnum) {
        when (menuItem) {
            MenuOptionEnum.HOME -> showHomeFragment()
            MenuOptionEnum.LOGOUT -> {
                DialogHelper.showDialog(
                    this,
                    getString(R.string.title_logoff),
                    getString(R.string.text_logoff_confirmation),
                    getString(R.string.global_yes),
                    getString(R.string.action_cancel),
                    listenerPositiveButton = { viewModel.onLogoutConfirmed() }
                )
            }
            MenuOptionEnum.PROFILE -> showProfileFragment()
            MenuOptionEnum.PREFERENCES -> showPreferencesFragment()
            MenuOptionEnum.INFORMATION -> showInformationsFragment()
            MenuOptionEnum.UNITIES -> showUnitsFragment()
            MenuOptionEnum.LINKS -> showLinksFragment()
            MenuOptionEnum.CIB -> {
                if (isGuest) {
                    showLoginDialog()
                } else {
                    notMigrated()
                }
            }
        }
    }

    private fun showHomeFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.content_layout, HomeFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun showProfileFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.content_layout, PerfilFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun showPreferencesFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.content_layout, PreferencesFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun showLinksFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.content_layout, LinksFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun showInformationsFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.content_layout, InformationsFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun showUnitsFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.content_layout, UnitsFragment())
            .addToBackStack(null)
            .commit()
    }

    fun showLoginDialog() {
        DialogHelper.showDialog(
            this,
            getString(R.string.title_login),
            getString(R.string.text_login),
            getString(R.string.global_yes),
            getString(R.string.action_cancel),
            listenerPositiveButton = { navigateToLogin() }
        )
    }

    private fun navigateToLogin() {
        LoginActivity.start(this)
    }

    private fun notMigrated() {
        Toast.makeText(this, "Em construção", Toast.LENGTH_SHORT).show()
    }
}
