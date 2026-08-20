package br.com.policlinsaude.ui.base

import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

/**
 * Classe base para todas as Activities com ViewBinding
 */
abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    protected lateinit var binding: VB

    protected abstract fun getViewBinding(): VB

    override fun setContentView(view: android.view.View?) {
        binding = getViewBinding()
        super.setContentView(binding.root)
    }
}
