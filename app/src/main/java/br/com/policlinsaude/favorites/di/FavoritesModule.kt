package br.com.policlinsaude.favorites.di

import br.com.domain.repository.Repository
import br.com.domain.usecase.GetFavoritesUseCase
import br.com.policlinsaude.favorites.navigator.FavoritesNavigator
import br.com.policlinsaude.favorites.navigator.FavoritesNavigatorImpl
import br.com.policlinsaude.favorites.presenter.FavoritesPresenter
import br.com.policlinsaude.favorites.presenter.FavoritesPresenterImpl
import br.com.policlinsaude.favorites.view.FavoritesActivity
import br.com.policlinsaude.favorites.view.FavoritesView
import dagger.Module
import dagger.Provides

/**
 * Created by lmiyagi on 3/27/18.
 */
@Module
class FavoritesModule {

    @Provides
    fun provideFavoritesView(activity: FavoritesActivity)
            : FavoritesView = activity

    @Provides
    fun provideFavoritesPresenter(view: FavoritesView,
                                  navigator: FavoritesNavigator,
                                  getFavoritesUseCase: GetFavoritesUseCase)
            : FavoritesPresenter = FavoritesPresenterImpl(view, navigator, getFavoritesUseCase)

    @Provides
    fun provideFavoritesNavigator(activity: FavoritesActivity)
            : FavoritesNavigator = FavoritesNavigatorImpl(activity)

    @Provides
    fun provideGetFavoritesUseCase(repository: Repository)
            : GetFavoritesUseCase = GetFavoritesUseCase(repository)
}

