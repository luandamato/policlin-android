package br.com.policlinsaude.ui.legacy.ownNetwork.di

import br.com.policlinsaude.ownNetwork.navigator.OwnNetworkNavigator
import br.com.policlinsaude.ownNetwork.navigator.OwnNetworkNavigatorImpl
import br.com.policlinsaude.ownNetwork.view.OwnNetworkActivity
import br.com.policlinsaude.ownNetwork.view.OwnNetworkFragment
import br.com.policlinsaude.ownNetwork.view.adapter.OwnNetworkAdapter
import dagger.Module
import dagger.Provides

@Module
class OwnNetworkFragmentModule {

    @Provides
    fun provideOwnNetworkNavigator(fragment: OwnNetworkFragment)
            : OwnNetworkNavigator = OwnNetworkNavigatorImpl(fragment.activity as OwnNetworkActivity)

    @Provides
    fun provideOwnNetworkAdapter(fragment: OwnNetworkFragment): OwnNetworkAdapter
            = OwnNetworkAdapter(fragment)

}

