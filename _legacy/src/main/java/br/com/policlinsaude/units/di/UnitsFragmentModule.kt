package br.com.policlinsaude.units.di

import br.com.policlinsaude.units.navigator.UnitsNavigator
import br.com.policlinsaude.units.navigator.UnitsNavigatorImpl
import br.com.policlinsaude.units.view.UnitsActivity
import br.com.policlinsaude.units.view.UnitsFragment
import br.com.policlinsaude.units.view.adapter.UnitsAdapter
import dagger.Module
import dagger.Provides

@Module
class UnitsFragmentModule {

    @Provides
    fun provideUnitsNavigator(fragment: UnitsFragment)
            : UnitsNavigator = UnitsNavigatorImpl(fragment.activity as UnitsActivity)

    @Provides
    fun provideUnitsAdapter(fragment: UnitsFragment): UnitsAdapter
            = UnitsAdapter(fragment)

}

