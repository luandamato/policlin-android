package br.com.policlinsaude.ui.activities.notifications

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import br.com.policlinsaude.R
import br.com.policlinsaude.databinding.ActivityNotificationBinding
import br.com.policlinsaude.ui.fragments.notifications.NotificationsFragment

/**
 * Tela de notificações (MVVM).
 *
 * Migrado do legado `NotificationActivity` — antes usava NavHostFragment +
 * Navigation Component; agora hospeda [NotificationsFragment] em um container
 * simples (mesmo padrão de Home/MenuActivity) com toolbar própria.
 *
 * Fluxo novo: UI → NotificationViewModel → AppRepository (MAPP_Notificacoes).
 */
class NotificationActivity : AppCompatActivity() {

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, NotificationActivity::class.java)
            activity.startActivity(intent)
        }

        fun start(context: android.content.Context) {
            val intent = Intent(context, NotificationActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.notificationToolbar.toolbar)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, NotificationsFragment.newInstance())
                .commit()
        }
    }

    fun showBackButton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.notificationToolbar.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    fun showButtonCancel(isShow: Boolean, listener: () -> Unit = {}) {
        binding.notificationToolbar.toolbar.menu.clear()
        if (isShow) {
            binding.notificationToolbar.toolbar.menu.apply {
                add(0, 0, 0, getString(R.string.action_cancel)).apply {
                    setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
                }.setOnMenuItemClickListener {
                    listener.invoke()
                    true
                }
            }
        }
    }

    fun showButtonEdit(isShow: Boolean, listener: () -> Unit = {}) {
        binding.notificationToolbar.toolbar.menu.clear()
        if (isShow) {
            binding.notificationToolbar.toolbar.menu.apply {
                add(0, 0, 0, getString(R.string.action_edit_notifications)).apply {
                    setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
                }.setOnMenuItemClickListener {
                    listener.invoke()
                    true
                }
            }
        }
    }
}