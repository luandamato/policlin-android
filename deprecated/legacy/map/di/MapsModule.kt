package br.com.policlinsaude.ui.legacy.map.di

import br.com.policlinsaude.map.navigator.MapsNavigator
import br.com.policlinsaude.map.navigator.MapsNavigatorImpl
import br.com.policlinsaude.map.presenter.MapsPresenter
import br.com.policlinsaude.map.presenter.MapsPresenterImpl
import br.com.policlinsaude.map.view.MapsActivity
import br.com.policlinsaude.map.view.MapsView
import dagger.Module
import dagger.Provides

/**
 * Created by lmiyagi on 05/04/18.
 */
@Module
class MapsModule {

    @Provides
    fun providePresenter(view: MapsView, navigator: MapsNavigator)
            : MapsPresenter = MapsPresenterImpl(view, navigator)

    @Provides
    fun provideView(activity: MapsActivity)
            : MapsView = activity

    @Provides
    fun provideNavigator(activity: MapsActivity)
            : MapsNavigator = MapsNavigatorImpl(activity)
}