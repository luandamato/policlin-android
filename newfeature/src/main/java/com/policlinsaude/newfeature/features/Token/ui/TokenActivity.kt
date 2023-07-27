package com.policlinsaude.newfeature.features.Token.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.policlinsaude.newfeature.features.Token.ui.ui.main.TokenFragment

class TokenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_token)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, TokenFragment.newInstance())
                .commitNow()
        }
    }
}