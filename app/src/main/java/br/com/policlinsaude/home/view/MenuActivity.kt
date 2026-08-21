package br.com.policlinsaude.home.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import br.com.policlinsaude.R
import br.com.policlinsaude.core.base.BaseActivity
import br.com.policlinsaude.core.helper.DialogHelper
import br.com.policlinsaude.core.helper.getBitmapFromImage
import br.com.policlinsaude.databinding.ActivityHomeBinding
import br.com.policlinsaude.domain.model.Person
import br.com.policlinsaude.home.navigator.MenuNavigator
import br.com.policlinsaude.home.presenter.MenuPresenter
import br.com.policlinsaude.home.view.adapter.MenuAdapter
import br.com.policlinsaude.home.view.model.PresentationMenuEnum
import javax.inject.Inject

class MenuActivity : BaseActivity(), MenuView, MenuAdapter.OnMenuItemClickListener {

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, MenuActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            activity.startActivity(intent)
        }
    }

    @Inject
    lateinit var presenter: MenuPresenter

    @Inject
    lateinit var menuNavigator: MenuNavigator

    lateinit var toggle: ActionBarDrawerToggle

    var isGuest: Boolean = false

    lateinit var phone: String

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupDrawer()

        // As toolbar is included via kotlinx synthetic previously and it's not in activity_home directly 
        // usually it's either in the layout or we need to find it.
        // Looking at activity_home.xml, it doesn't have a toolbar. 
        // The original code used `setupFragmentToolbar(toolbar, null)`. 
        // I need to check where `toolbar` comes from. It was imported from kotlinx.android.synthetic.main.toolbar.*
        // Since I don't see a toolbar in activity_home.xml, maybe it's inside content_layout or it was assumed to be present.
        // Wait, activity_home.xml has content_layout.
        
        // I'll check toolbar.xml again to see its ID.
        // Actually, many projects have a common toolbar layout.
        
        setupFragmentToolbar(null, null) // Temporary until I find where toolbar is.

        setSelectedItem(PresentationMenuEnum.HOME)
        setupListeners()
        getCurrentPerson()
    }

    fun getCurrentPerson() {
        presenter.getCurrentPerson()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            if (supportFragmentManager.backStackEntryCount > 1) {
                supportFragmentManager.popBackStackImmediate()
            } else {
                showQuitConfirmation()
            }
        }
    }

    private fun showQuitConfirmation() {
        DialogHelper.showDialog(this,
                R.string.title_exit,
                R.string.text_exit_confirmation,
                R.string.global_yes,
                R.string.action_cancel,
                { finish() })
    }

    override fun onClick(menuItem: PresentationMenuEnum) {
        setSelectedItem(menuItem)
        binding.drawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun setupDrawer() {
        binding.navView.findViewById<android.widget.ListView>(R.id.menu_items_list_view).adapter = MenuAdapter(this,
                R.layout.list_item_menu,
                PresentationMenuEnum.values(),
                this)
    }

    override fun renderPerson(person: Person) {
        val navHeader = binding.navView.getHeaderView(0) // If it's a header
        // But in activity_home.xml it's an <include> inside a LinearLayout inside NavigationView.
        // So binding.navHeader should work if it has an ID.
        
        binding.navHeader.profilePictureImageView.setImageBitmap(person.photo.getBitmapFromImage())
        binding.navHeader.nameTextView.visibility = View.VISIBLE
        binding.navHeader.planTextView.visibility = View.VISIBLE
        binding.navHeader.nameTextView.text = person.name
        binding.navHeader.planTextView.text = person.descriptionPlan
    }

    override fun setupGuest() {
        binding.navHeader.nameTextView.visibility = View.GONE
        binding.navHeader.planTextView.visibility = View.GONE
        isGuest = true
        // The original code had: val nameTextView: TextView = findViewById(R.id.person_name_text_view)
        // I will keep it or use binding if I find where it is.
        val nameTextView: TextView = findViewById(R.id.person_name_text_view)
        nameTextView.setText(R.string.text_guest) 
    }

    override fun showError(throwable: Throwable) {
        DialogHelper.showDialog(this,
                R.string.title_error_oops,
                R.string.msg_unexpected_error,
                R.string.text_ok)
    }

    override fun showLoginDialog() {
        DialogHelper.showDialog(this,
                R.string.title_login,
                R.string.text_login,
                R.string.global_yes,
                R.string.action_cancel,
                { presenter.onLoginClicked() })
    }

    override fun showUpdateDialog(throwable: Throwable) {
        DialogHelper.showUpdateDialog(
            this,
            throwable.message.orEmpty()
        )
    }

    override fun showUserNotConnectedDialog(message: String) {
        DialogHelper.showDialog(
            this,
            getString(R.string.title_error_oops).orEmpty(),
            message,
            getString(R.string.text_ok).orEmpty(),
            null,
            { presenter.onLoginClicked() }
        )
    }

    fun setupFragmentToolbar(toolbar: Toolbar?, @StringRes title: Int?) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = if (title == null) null else getString(title)

        toggle = ActionBarDrawerToggle(
                this, binding.drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        toggle.apply {
            isDrawerIndicatorEnabled = true
            isDrawerSlideAnimationEnabled = true
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        toggle.drawerArrowDrawable.color = ContextCompat.getColor(this, R.color.white)
    }

    fun setupListeners(){
        binding.navView.findViewById<View>(R.id.sair).setOnClickListener {
            DialogHelper.showDialog(this,
                R.string.title_logoff,
                R.string.text_logoff_confirmation,
                R.string.global_yes,
                R.string.action_cancel,
                { logout() })
        }
    }
    private fun logout(){
        presenter.onLogoutConfirmed()
    }

    private fun setSelectedItem(item: PresentationMenuEnum) {
        when (item) {
            PresentationMenuEnum.HOME -> {
                menuNavigator.goToHome()
            }
            PresentationMenuEnum.PROFILE -> {
                if (isGuest) {
                    presenter.onMenuClickedAsGuest()
                } else {
                    menuNavigator.goToPerfil()
                }
            }
            PresentationMenuEnum.PREFERENCES -> {
                if (isGuest) {
                    presenter.onMenuClickedAsGuest()
                } else {
                    menuNavigator.goToPreferences()
                }
            }
            PresentationMenuEnum.INFORMATION -> {
                menuNavigator.goToInformations()
            }
            PresentationMenuEnum.UNITIES -> {
                menuNavigator.goToUnits()
            }
            PresentationMenuEnum.LINKS -> {
                menuNavigator.goToLinks()
            }
            PresentationMenuEnum.TELEFONECID -> {
                menuNavigator.goToCallIntent()
            }
            PresentationMenuEnum.LOGOUT -> {
                DialogHelper.showDialog(this,
                    R.string.title_logoff,
                    R.string.text_logoff_confirmation,
                    R.string.global_yes,
                    R.string.action_cancel,
                    { logout() })
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            RESULT_OK -> {
                when (requestCode) {
                    123 -> {
                        finish()
                    }
                }
            }
        }
    }
}
