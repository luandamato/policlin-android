package br.com.policlinsaude.core.application

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import br.com.policlinsaude.data.helper.ModuleDataHelper
import br.com.policlinsaude.core.di.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject
import androidx.multidex.MultiDex
import br.com.policlinsaude.otherFeatures.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.core.context.startKoin

class PoliclinSaudeApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

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

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    private fun injectDependencies() {
        DaggerApplicationComponent
                .builder()
                .application(this)
                .build()
                .inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> = activityDispatchingAndroidInjector

}
