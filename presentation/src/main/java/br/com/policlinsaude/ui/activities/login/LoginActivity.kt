package br.com.policlinsaude.ui.activities.login

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var buttonEnter: Button
    private lateinit var buttonForgotPassword: Button
    private lateinit var buttonNotHasPassword: Button
    private lateinit var buttonIamNotClient: Button
    private lateinit var editTextRegister: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var textViewMsgWhenEntering: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(br.com.policlinsaude.R.layout.activity_login)

        buttonEnter = findViewById(br.com.policlinsaude.R.id.buttonEnter)
        buttonForgotPassword = findViewById(br.com.policlinsaude.R.id.buttonForgotPassword)
        buttonNotHasPassword = findViewById(br.com.policlinsaude.R.id.buttonNotHasPassword)
        buttonIamNotClient = findViewById(br.com.policlinsaude.R.id.buttonIamNotClient)
        editTextRegister = findViewById(br.com.policlinsaude.R.id.editTextRegister)
        editTextPassword = findViewById(br.com.policlinsaude.R.id.editTextPassword)
        textViewMsgWhenEntering = findViewById(br.com.policlinsaude.R.id.textViewMsgWhenEntering)

        setupUI()
    }

    private fun setupUI() {
        buttonEnter.setOnClickListener { login() }
        buttonForgotPassword.setOnClickListener { showMessage("Recuperar senha - em desenvolvimento") }
        buttonNotHasPassword.setOnClickListener { showMessage("Registrar novo usuário - em desenvolvimento") }
        buttonIamNotClient.setOnClickListener { showMessage("Não sou cliente - em desenvolvimento") }
    }

    private fun login() {
        val email = editTextRegister.text.toString()
        val password = editTextPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            showMessage("Preencha email e senha")
            return
        }

        showMessage("Login em desenvolvimento para: $email")
    }

    private fun showMessage(message: String) {
        textViewMsgWhenEntering.text = message
        textViewMsgWhenEntering.visibility = View.VISIBLE
    }
}
