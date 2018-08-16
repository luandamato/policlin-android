package br.com.policlinsaude.core.di

import android.app.Application
import br.com.data.di.DataModule
import br.com.data.di.NetworkingModule
import br.com.policlinsaude.core.application.PoliclinSaudeApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, ApplicationModule::class,
    NetworkingModule::class, DataModule::class, ActivitiesBuilder::class,
    UserModule::class, PreferenceModule::class])

interface ApplicationComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): ApplicationComponent

    }

    fun inject(target: PoliclinSaudeApplication)

}

