package br.com.policlinsaude.home.view

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import br.com.domain.model.Person
import br.com.policlinsaude.R
import br.com.policlinsaude.core.base.BaseActivity
import br.com.policlinsaude.core.helper.DialogHelper
import br.com.policlinsaude.core.helper.getBitmapFromImage
import br.com.policlinsaude.home.navigator.MenuNavigator
import br.com.policlinsaude.home.presenter.MenuPresenter
import br.com.policlinsaude.home.view.adapter.MenuAdapter
import br.com.policlinsaude.home.view.model.PresentationMenuEnum
import com.policlinsaude.newfeature.features.deleteUser.ui.Activity.DeleteUserActivity
import com.policlinsaude.newfeature.features.guidAuthorizer.ui.fragments.ProcessRequestFragment
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.nav_header_home.view.*
import kotlinx.android.synthetic.main.toolbar.*
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupDrawer()

        setupFragmentToolbar(toolbar, null)

        setSelectedItem(PresentationMenuEnum.HOME)
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
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
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
        drawer_layout.closeDrawer(GravityCompat.START)
    }

    private fun setupDrawer() {
        menu_items_list_view.adapter = MenuAdapter(this,
                R.layout.list_item_menu,
                PresentationMenuEnum.values(),
                this)
    }

    override fun renderPerson(person: Person) {
        try {
            nav_header.profile_picture_image_view.setImageBitmap(person.photo.getBitmapFromImage())
        } catch (e: Exception) {
            e.printStackTrace()
            // ignored
        }
        nav_header.name_text_view.visibility = View.VISIBLE
        nav_header.plan_text_view.visibility = View.VISIBLE
        nav_header.name_text_view.text = person.name
        nav_header.plan_text_view.text = person.descriptionPlan
    }

    override fun setupGuest() {
        nav_header.name_text_view.visibility = View.GONE
        nav_header.plan_text_view.visibility = View.GONE
        isGuest = true
        val nameTextView: TextView = findViewById(R.id.person_name_text_view)

        nameTextView.setText(R.string.text_guest) //alteração da parte preta

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
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        toggle.apply {
            isDrawerIndicatorEnabled = true
            isDrawerSlideAnimationEnabled = true
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        toggle.drawerArrowDrawable.color = ContextCompat.getColor(this, R.color.white)

        drawer_layout.addDrawerListener(toggle)
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
            PresentationMenuEnum.DELETE -> {
                if (isGuest) {
                    presenter.onMenuClickedAsGuest()
                } else {
                    val intent = Intent(this, DeleteUserActivity::class.java)
                    startActivityForResult(intent, 123)
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
