package br.com.policlinsaude.login.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Html
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import br.com.domain.exception.MessageErrorException
import br.com.policlinsaude.BuildConfig
import br.com.policlinsaude.R
import br.com.policlinsaude.core.base.BaseActivity
import br.com.policlinsaude.core.helper.IntentHelper
import br.com.policlinsaude.login.presenter.LoginPresenter
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        presenter.checkHasToken()

        val textViewMsgWhenEntering = findViewById<TextView>(R.id.textViewMsgWhenEntering)
        textViewMsgWhenEntering.setText(Html.fromHtml(getString(R.string.msg_when_entering_you_allow)), TextView.BufferType.SPANNABLE)
    }

    override fun onPersonNotFound() {
        addValidationFields()
        setOnClickListeners()
        //setupDebug()
    }

    private fun addValidationFields() {
        awesomeValidation.addValidation(editTextRegister,
                RegexTemplate.NOT_EMPTY, getString(R.string.text_field_required))
        awesomeValidation.addValidation(editTextPassword,
                RegexTemplate.NOT_EMPTY, getString(R.string.text_field_required))
        awesomeValidation.addValidation(editTextOrder,
                RegexTemplate.NOT_EMPTY, getString(R.string.text_field_required))
    }

    private fun setOnClickListeners() {
        buttonEnter.setOnClickListener {
            if (awesomeValidation.validate()) {
                presenter.clickedButtonEnter()
            }
        }
        buttonForgotPassword.setOnClickListener {
            presenter.clickedButtonForgotPassword()
        }
        buttonNotHasPassword.setOnClickListener {
            presenter.clickedButtonNotHasPassword()
        }
        buttonIamNotClient.setOnClickListener {
            presenter.clickedButtonIamNotClient()
        }

        imgEye.setOnClickListener {
            presenter.clickedEye()
        }

        textViewMsgWhenEntering.setOnClickListener {
            presenter.clickedLink()
        }


    }



    /*      switch ( event.getAction() ) {
                case MotionEvent.ACTION_DOWN:
                   editText.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
                case MotionEvent.ACTION_UP:
                    editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
                }
                return true;
    * */

    override fun showDialogError(it: Throwable) {
        val listener = {
            presenter.clickedButtonEnter()
        }
        showDialogTryAgain(listenerPositiveButton = listener,
                message = if (it is MessageErrorException) it.message!! else "")
    }

    override fun showLoginLoading() {
        login_progressbar.visibility = View.VISIBLE
    }

    override fun hideLoginLoading() {
        login_progressbar.visibility = View.GONE
    }

    override fun getRegister(): String = editTextRegister.text.toString()

    override fun getPassword(): String = editTextPassword.text.toString()

    override fun getOrder(): String = editTextOrder.text.toString()

    override fun showButtonEnter() {
        buttonEnter.visibility = View.VISIBLE
    }

    override fun hideButtonEnter() {
        buttonEnter.visibility = View.GONE
    }

    override fun showLoading() {
        loading_container.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loading_container.visibility = View.GONE
    }

    override fun changeEye() {

        if (hided){
            Log.d("LOGIN","ESTAVA HIDED")
            imgEye.setImageResource(R.drawable.ic_action_eye_open)
            //editTextPassword.setInputType (InputType.TYPE_CLASS_TEXT)
            editTextPassword.setTransformationMethod(null);
            hided = false
        }
         else{
            Log.d("LOGIN","ESTAVA VISIBLE")
            imgEye.setImageResource(R.drawable.ic_action_eye_closed)
           // editTextPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD)
            editTextPassword.setTransformationMethod(PasswordTransformationMethod())
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
