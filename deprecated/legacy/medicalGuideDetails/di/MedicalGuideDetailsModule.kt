package br.com.policlinsaude.ui.legacy.medicalGuideDetails.di

import br.com.domain.repository.Repository
import br.com.domain.usecase.AddToFavoriteUseCase
import br.com.domain.usecase.GetFavoritesUseCase
import br.com.domain.usecase.GetPlansUseCase
import br.com.domain.usecase.RemoveFromFavoritesUseCase
import br.com.policlinsaude.favorites.navigator.FavoritesNavigator
import br.com.policlinsaude.favorites.presenter.FavoritesPresenter
import br.com.policlinsaude.favorites.view.FavoritesView
import br.com.policlinsaude.medicalGuideDetails.navigator.MedicalGuideDetailsNavigator
import br.com.policlinsaude.medicalGuideDetails.navigator.MedicalGuideDetailsNavigatorImpl
import br.com.policlinsaude.medicalGuideDetails.presenter.FavoritesStorePrefsPresenter
import br.com.policlinsaude.medicalGuideDetails.presenter.FavoritesStorePrefsPresenterImpl
import br.com.policlinsaude.medicalGuideDetails.presenter.MedicalGuideDetailsPresenter
import br.com.policlinsaude.medicalGuideDetails.presenter.MedicalGuideDetailsPresenterImpl
import br.com.policlinsaude.medicalGuideDetails.view.MedicalGuideDetailsActivity
import br.com.policlinsaude.medicalGuideDetails.view.MedicalGuideDetailsView
import br.com.policlinsaude.medicalGuideDetails.view.adapter.MedicalGuideDetailsAdapter
import dagger.Module
import dagger.Provides

@Module
class MedicalGuideDetailsModule {

    @Provides
    fun providesMedicalGuideDetailsPresenter(navigator: MedicalGuideDetailsNavigator,
                                             getPlansUseCase: GetPlansUseCase,
                                             view: MedicalGuideDetailsView,
                                             addToFavoriteUseCase: AddToFavoriteUseCase,
                                             removeFromFavoritesUseCase: RemoveFromFavoritesUseCase)
            : MedicalGuideDetailsPresenter = MedicalGuideDetailsPresenterImpl(navigator = navigator,
            getPlansUseCase = getPlansUseCase,
            view = view,
            addToFavoriteUseCase = addToFavoriteUseCase,
            removeFromFavoritesUseCase = removeFromFavoritesUseCase)

    @Provides
    fun providesFavoritesStorePrefsPresenter(view: MedicalGuideDetailsView,
                                             getFavoritesUseCase1: GetFavoritesUseCase)
            :FavoritesStorePrefsPresenter = FavoritesStorePrefsPresenterImpl( view = view, getFavoritesUseCase = getFavoritesUseCase1)




        @Provides
    fun provideMedicalGuideDetailsNavigator(activity: MedicalGuideDetailsActivity)
            : MedicalGuideDetailsNavigator = MedicalGuideDetailsNavigatorImpl(activity)

    @Provides
    fun provideMedicalGuideDetailsView(activity: MedicalGuideDetailsActivity)
            : MedicalGuideDetailsView = activity

    @Provides
    fun provideMedicalMedicalGuideDetailsAdapter(activity: MedicalGuideDetailsActivity)
            : MedicalGuideDetailsAdapter = MedicalGuideDetailsAdapter()

    @Provides
    fun provideGetPlansUseCase(repository: Repository)
            : GetPlansUseCase = GetPlansUseCase(repository)

    @Provides
    fun provideAddToFavoriteUseCase(repository: Repository)
            : AddToFavoriteUseCase = AddToFavoriteUseCase(repository)

    @Provides
    fun provideRemoveFromFavoritesUseCase(repository: Repository)
            : RemoveFromFavoritesUseCase = RemoveFromFavoritesUseCase(repository)

    @Provides
    fun provideGetFavoritesToStoreUseCase(repository: Repository)
            : GetFavoritesUseCase = GetFavoritesUseCase(repository)





/*    @Provides
    fun provideGetMedicalGuideOptionsUseCase(repository: Repository)
            : GetMedicalGuideOptionsUseCase = GetMedicalGuideOptionsUseCase(repository = repository)*/

}