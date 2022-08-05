package br.com.policlinsaude.core.base

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import br.com.policlinsaude.core.application.PoliclinSaudeApplication
import br.com.policlinsaude.core.helper.DialogHelper
import br.com.policlinsaude.core.helper.InvalidData
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.google.firebase.FirebaseApp
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.toolbar.*
import java.lang.Exception

abstract class BaseActivity : AppCompatActivity(), HasSupportFragmentInjector {

    lateinit var awesomeValidation: AwesomeValidation

    override fun onCreate(savedInstanceState: Bundle?) {
        performDependencyInjection()
        awesomeValidation = AwesomeValidation(ValidationStyle.BASIC)
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState)
    }

    private fun performDependencyInjection() {
        AndroidInjection.inject(this)
    }

    fun setupToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            with(it){
                setDisplayHomeAsUpEnabled(true)
                setHomeButtonEnabled(true)
            }
        }

        try {
            (toolbar.getChildAt(0) as TextView).textSize = 16f
        } catch (e: Exception) {}
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when (item.itemId) {
                android.R.id.home -> {
                    onBackPressed()
                    Log.d("FILTRO", "BACKPRESSED: onBackPressed()")
                    return true
                }else -> {}
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun showDialogTryAgain(listenerPositiveButton: ()-> Unit,
                     message: String = InvalidData.UNINITIALIZED.getString()){
        DialogHelper.showDialogTryAgain(context = this,
                listenerPositiveButton = listenerPositiveButton, message = message)
    }

    fun showToast(@StringRes message: Int) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showError(message: String = InvalidData.UNINITIALIZED.getString()) {
        DialogHelper.showErrorDialog(this, message)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment>
            = (application as PoliclinSaudeApplication).fragmentInjector

}