package br.com.policlinsaude.core.base

import android.content.Context
import dagger.android.support.AndroidSupportInjection

abstract class BaseFragmentWithInject : BaseFragment() {

    override fun onAttach(context: Context) {
        injectDependencies()
        super.onAttach(context)
    }

    fun injectDependencies() {
        AndroidSupportInjection.inject(this)
    }

}