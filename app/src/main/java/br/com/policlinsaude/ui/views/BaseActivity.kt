package br.com.policlinsaude.ui.views

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import br.com.policlinsaude.ui.dialogs.DialogHelper

/**
 * Activity base da nova arquitetura (sem Dagger).
 * Migrado/adaptado de `_legacy/.../core/base/BaseActivity.kt` mantendo:
 * - tratamento de insets (status/navigation/IME)
 * - helpers de toolbar e diálogos.
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            window.navigationBarColor = Color.TRANSPARENT
        }
        super.onCreate(savedInstanceState)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        applyInsetsToRootContent()
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)
        applyInsetsToRootContent()
    }

    private fun applyInsetsToRootContent() {
        val contentView = findViewById<View>(android.R.id.content)
        ViewCompat.setOnApplyWindowInsetsListener(contentView) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            view.updatePadding(
                left = systemBars.left,
                top = systemBars.top,
                right = systemBars.right,
                bottom = maxOf(systemBars.bottom, imeInsets.bottom)
            )
            insets
        }
        ViewCompat.requestApplyInsets(contentView)
    }

    protected fun setupToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun showToast(@StringRes message: Int) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showError(message: String = "") {
        DialogHelper.showErrorDialog(this, message)
    }

    fun showDialogTryAgain(listenerPositiveButton: () -> Unit = {}, message: String = "") {
        DialogHelper.showDialogTryAgain(this, message) { listenerPositiveButton() }
    }

    fun showDialogUpdateApp(message: String = "") {
        DialogHelper.showUpdateDialog(this, message)
    }
}