package br.com.policlinsaude.core.base

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import br.com.policlinsaude.core.helper.DialogHelper
import br.com.policlinsaude.core.helper.InvalidData
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.google.firebase.FirebaseApp
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity(), HasAndroidInjector {

    lateinit var awesomeValidation: AwesomeValidation

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        performDependencyInjection()

        super.onCreate(savedInstanceState)

        awesomeValidation = AwesomeValidation(ValidationStyle.BASIC)

        FirebaseApp.initializeApp(this)
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