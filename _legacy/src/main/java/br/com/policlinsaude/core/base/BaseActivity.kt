package br.com.policlinsaude.core.base

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.google.firebase.FirebaseApp
import br.com.policlinsaude.core.helper.DialogHelper
import br.com.policlinsaude.core.helper.InvalidData

abstract class BaseActivity : AppCompatActivity(), HasAndroidInjector {

    lateinit var awesomeValidation: AwesomeValidation

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        performDependencyInjection()

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            window.navigationBarColor = Color.TRANSPARENT
        }

        super.onCreate(savedInstanceState)

        awesomeValidation = AwesomeValidation(ValidationStyle.BASIC)

        FirebaseApp.initializeApp(this)
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

    private fun performDependencyInjection() {
        AndroidInjection.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }

    protected fun setupToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }

        try {
            (toolbar.getChildAt(0) as? TextView)?.textSize = 14f
        } catch (e: Exception) {
            Log.w(
                "BaseActivity",
                "Não foi possível configurar o tamanho do título",
                e
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                Log.d(
                    "BaseActivity",
                    "BACKPRESSED: OnBackPressedDispatcher"
                )
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    fun showDialogTryAgain(
        listenerPositiveButton: () -> Unit,
        message: String = InvalidData.UNINITIALIZED.getString()
    ) {
        DialogHelper.showDialogTryAgain(
            context = this,
            listenerPositiveButton = listenerPositiveButton,
            message = message
        )
    }

    fun showDialogUpdateApp(
        message: String = InvalidData.UNINITIALIZED.getString()
    ) {
        DialogHelper.showUpdateDialog(
            context = this,
            message = message
        )
    }

    fun showToast(@StringRes message: Int) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showError(
        message: String = InvalidData.UNINITIALIZED.getString()
    ) {
        DialogHelper.showErrorDialog(this, message)
    }
}