package br.com.policlinsaude.core.di

import br.com.policlinsaude.editPassword.di.EditPasswordModule
import br.com.policlinsaude.editPassword.view.EditPasswordActivity
import br.com.policlinsaude.editPhone.di.EditPhoneModule
import br.com.policlinsaude.editPhone.view.EditPhoneActivity
import br.com.policlinsaude.favorites.di.FavoritesModule
import br.com.policlinsaude.favorites.view.FavoritesActivity
import br.com.policlinsaude.forgotPassword.di.ForgotPasswordModule
import br.com.policlinsaude.forgotPassword.view.ForgotPasswordActivity
import br.com.policlinsaude.healthInsurancePhoto.di.HealthInsurancePhotoModule
import br.com.policlinsaude.healthInsurancePhoto.view.HealthInsurancePhotoActivity
import br.com.policlinsaude.home.di.HomeModule
import br.com.policlinsaude.home.di.MenuModule
import br.com.policlinsaude.home.view.HomeFragment
import br.com.policlinsaude.home.view.MenuActivity
import br.com.policlinsaude.login.di.LoginModule
import br.com.policlinsaude.login.view.LoginActivity
import br.com.policlinsaude.map.di.MapsModule
import br.com.policlinsaude.map.view.MapsActivity
import br.com.policlinsaude.medicalGuideDetails.di.MedicalGuideDetailsModule
import br.com.policlinsaude.medicalGuideDetails.view.MedicalGuideDetailsActivity
import br.com.policlinsaude.medicalGuideList.di.MedicalGuideListModule
import br.com.policlinsaude.medicalGuideList.view.MedicalGuideListActivity
import br.com.policlinsaude.medicalGuideOptions.di.MedicalGuideOptionsModule
import br.com.policlinsaude.medicalGuideOptions.view.MedicalGuideOptionsActivity
import br.com.policlinsaude.notHasPassword.di.NotHasPasswordModule
import br.com.policlinsaude.notHasPassword.view.NotHasPasswordActivity
import br.com.policlinsaude.ownNetwork.di.OwnNetworkFragmentModule
import br.com.policlinsaude.ownNetwork.di.OwnNetworkModule
import br.com.policlinsaude.ownNetwork.view.OwnNetworkActivity
import br.com.policlinsaude.ownNetwork.view.OwnNetworkFragment
import br.com.policlinsaude.perfil.di.PerfilModule
import br.com.policlinsaude.perfil.view.PerfilFragment
import br.com.policlinsaude.preferences.di.PreferencesModule
import br.com.policlinsaude.preferences.view.PreferencesFragment
import br.com.policlinsaude.units.di.UnitsFragmentModule
import br.com.policlinsaude.units.di.UnitsModule
import br.com.policlinsaude.units.view.UnitsActivity
import br.com.policlinsaude.units.view.UnitsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivitiesBuilder {

    @ContributesAndroidInjector(modules = [(LoginModule::class)])
    abstract fun loginActivity(): LoginActivity

    @ContributesAndroidInjector(modules = [(ForgotPasswordModule::class)])
    abstract fun forgotPasswordActivity(): ForgotPasswordActivity

    @ContributesAndroidInjector(modules = [(NotHasPasswordModule::class)])
    abstract fun notHasPasswordActivity(): NotHasPasswordActivity

    @ContributesAndroidInjector(modules = [(MenuModule::class)])
    abstract fun menuActivity(): MenuActivity

    @ContributesAndroidInjector(modules = [(HomeModule::class)])
    abstract fun homeFragment(): HomeFragment

    @ContributesAndroidInjector(modules = [(PerfilModule::class)])
    abstract fun perfilFragment(): PerfilFragment

    @ContributesAndroidInjector(modules = [(PreferencesModule::class)])
    abstract fun preferencesFragment(): PreferencesFragment

    @ContributesAndroidInjector(modules = [(HealthInsurancePhotoModule::class)])
    abstract fun healthInsurancePhotoActivity(): HealthInsurancePhotoActivity

    @ContributesAndroidInjector(modules = [(MedicalGuideOptionsModule::class)])
    abstract fun medicalGuideOptionsActivity(): MedicalGuideOptionsActivity

    @ContributesAndroidInjector(modules = [(MedicalGuideListModule::class)])
    abstract fun medicalGuideListActivity(): MedicalGuideListActivity

    @ContributesAndroidInjector(modules = [(MedicalGuideDetailsModule::class)])
    abstract fun medicalGuideDetailsActivity(): MedicalGuideDetailsActivity

    @ContributesAndroidInjector(modules = [(OwnNetworkModule::class)])
    abstract fun ownNetworkActivity(): OwnNetworkActivity

    @ContributesAndroidInjector(modules = [(OwnNetworkFragmentModule::class)])
    abstract fun ownNetworkFragment(): OwnNetworkFragment

    @ContributesAndroidInjector(modules = [(FavoritesModule::class)])
    abstract fun favoriteActivity(): FavoritesActivity

    @ContributesAndroidInjector(modules = [(EditPasswordModule::class)])
    abstract fun editPasswordActivity(): EditPasswordActivity

    @ContributesAndroidInjector(modules = [(EditPhoneModule::class)])
    abstract fun editEmailActivity(): EditPhoneActivity

    @ContributesAndroidInjector(modules = [(MapsModule::class)])
    abstract fun mapsActivity(): MapsActivity


    @ContributesAndroidInjector(modules = [(UnitsModule::class)])
    abstract fun unitsActivity(): UnitsActivity


    @ContributesAndroidInjector(modules = [(UnitsFragmentModule::class)])
    abstract fun unitsFragment(): UnitsFragment





}
