package br.com.policlinsaude.login.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.text.Html
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.TextView
import br.com.policlinsaude.domain.exception.MessageErrorException
import br.com.policlinsaude.R
import br.com.policlinsaude.core.base.BaseActivity
import br.com.policlinsaude.core.helper.IntentHelper
import br.com.policlinsaude.databinding.ActivityLoginBinding
import br.com.policlinsaude.login.presenter.LoginPresenter
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import javax.inject.Inject
import android.telephony.TelephonyManager

import android.os.Build
import android.provider.Settings
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import java.util.*


class LoginActivity : BaseActivity(), LoginView {


    companion object {

        // teste de GIT
        fun start(activity: Activity) {
            val intent = Intent(activity, LoginActivity::class.java)

            activity.startActivity(intent)
        }

        fun start(fragment: Fragment) {
        /*    val intent = Intent(fragment.context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            fragment.startActivity(intent)*/
        }
    }

    @Inject
    lateinit var presenter: LoginPresenter
    private var hided: Boolean = true

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter.checkHasToken()

        binding.textViewMsgWhenEntering.setText(Html.fromHtml(getString(R.string.msg_when_entering_you_allow)), TextView.BufferType.SPANNABLE)
    }

    override fun onBackPressed() {
        val a = Intent(Intent.ACTION_MAIN)
        a.addCategory(Intent.CATEGORY_HOME)
        a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(a)
    }

    override fun onPersonNotFound() {
        addValidationFields()
        setOnClickListeners()
        removeCarteirinha()
    }

    private fun removeCarteirinha() {
        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("photosPref", "")
        editor.putString("photoVersoPref", "")
        editor.apply()
    }

    private fun addValidationFields() {
        awesomeValidation.addValidation(binding.editTextRegister,
                RegexTemplate.NOT_EMPTY, getString(R.string.text_field_required))
        awesomeValidation.addValidation(binding.editTextPassword,
                RegexTemplate.NOT_EMPTY, getString(R.string.text_field_required))
        awesomeValidation.addValidation(binding.editTextOrder,
                RegexTemplate.NOT_EMPTY, getString(R.string.text_field_required))
    }

    private fun setOnClickListeners() {
        binding.buttonEnter.setOnClickListener {
            getToken()
        }
        binding.buttonForgotPassword.setOnClickListener {
            presenter.clickedButtonForgotPassword()
        }
        binding.buttonNotHasPassword.setOnClickListener {
            presenter.clickedButtonNotHasPassword()
        }
        binding.buttonIamNotClient.setOnClickListener {
            presenter.clickedButtonIamNotClient()
        }

        binding.imgEye.setOnClickListener {
            presenter.clickedEye()
        }

        binding.textViewMsgWhenEntering.setOnClickListener {
            presenter.clickedLink()
        }
    }

    @SuppressLint("MissingPermission", "HardwareIds")
    private fun getToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("Token Failed", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            if (awesomeValidation.validate()) {
                presenter.clickedButtonEnter(token)
            }
        })
    }

    override fun showDialogError(it: Throwable) {
        val listener = {
            getToken()
        }
        if(it.message?.contains("450") == true) {
            val message = it.message?.split("-").orEmpty()
            showDialogUpdateApp(message = message[1])
        } else {
            showError(
                message = it.message.orEmpty()
            )
        }
    }

    override fun showLoginLoading() {
        binding.loginProgressbar.visibility = View.VISIBLE
    }

    override fun hideLoginLoading() {
        binding.loginProgressbar.visibility = View.GONE
    }

    override fun getRegister(): String = binding.editTextRegister.text.toString()

    override fun getPassword(): String = binding.editTextPassword.text.toString()

    override fun getOrder(): String = binding.editTextOrder.text.toString()

    override fun showButtonEnter() {
        binding.buttonEnter.visibility = View.VISIBLE
    }

    override fun hideButtonEnter() {
        binding.buttonEnter.visibility = View.GONE
    }

    override fun showLoading() {
        binding.loadingContainer.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.loadingContainer.visibility = View.GONE
    }

    override fun changeEye() {

        if (hided){
            Log.d("LOGIN","ESTAVA HIDED")
            binding.imgEye.setImageResource(R.drawable.ic_action_eye_open)
            //binding.editTextPassword.setInputType (InputType.TYPE_CLASS_TEXT)
            binding.editTextPassword.setTransformationMethod(null);
            hided = false
        }
         else{
            Log.d("LOGIN","ESTAVA VISIBLE")
            binding.imgEye.setImageResource(R.drawable.ic_action_eye_closed)
           // binding.editTextPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD)
            binding.editTextPassword.setTransformationMethod(PasswordTransformationMethod())
            hided = true
        }

    }

    override fun clickedLink() {

        IntentHelper.openUrlInBrowser(this, getString(R.string.url_privacy))
    }

    /* private fun setupDebug() {
         if (BuildConfig.DEBUG) {
             editTextRegister.setText("90540")
             editTextOrder.setText("0")
             editTextPassword.setText("PS10631817")
         }
     }*/
}
