package br.com.policlinsaude.core.application

import android.app.Application
import android.content.Context
import br.com.policlinsaude.core.di.DaggerApplicationComponent
import br.com.policlinsaude.data.helper.ModuleDataHelper
import br.com.policlinsaude.otherFeatures.di.appModules
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.core.context.startKoin

class PoliclinSaudeApplication : Application(), HasAndroidInjector {

    @Inject
    lateinit var androidDispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()

        injectDependencies()

        ModuleDataHelper.configureDatabase(this)

        startKoin {
            androidContext(this@PoliclinSaudeApplication)
            androidFileProperties()
            modules(appModules)
        }
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return androidDispatchingAndroidInjector
    }

    private fun injectDependencies() {
        DaggerApplicationComponent
            .builder()
            .application(this)
            .build()
            .inject(this)
    }
}