package br.com.policlinsaude.ui.activities

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * Activity placeholder que permite rodar o app na nova arquitectura.
 * Mostra apenas um texto na tela; se amplia (e a carpeta
 * `ui/activities/<feature>` se preenche) conforme se migre as features
 * desde `_legacy`.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val title = TextView(this).apply {
            text = "Policlín Saúde"
            textSize = 24f
        }

        val subtitle = TextView(this).apply {
            text = "Nova arquitetura — migración em curso"
            textSize = 14f
        }

        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            title.setLayoutParams(LinearLayout.LayoutParams(-1, -2))
            subtitle.setLayoutParams(LinearLayout.LayoutParams(-1, -2))
        }

        container.addView(title)
        container.addView(subtitle)

        setContentView(container)
        supportActionBar?.hide()
    }
}