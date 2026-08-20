package br.com.policlinsaude.healthInsurancePhoto.di

import br.com.domain.repository.Repository
import br.com.domain.usecase.GetHealthInsurancePhotoUseCase
import br.com.policlinsaude.healthInsurancePhoto.navigator.HealthInsurancePhotoNavigator
import br.com.policlinsaude.healthInsurancePhoto.navigator.HealthInsurancePhotoNavigatorImpl
import br.com.policlinsaude.healthInsurancePhoto.presenter.HealthInsurancePhotoPresenter
import br.com.policlinsaude.healthInsurancePhoto.presenter.HealthInsurancePhotoPresenterImpl
import br.com.policlinsaude.healthInsurancePhoto.view.HealthInsurancePhotoActivity
import br.com.policlinsaude.healthInsurancePhoto.view.HealthInsurancePhotoView
import br.com.policlinsaude.healthInsurancePhoto.view.adapter.PhotoPageAdapter
import br.com.policlinsaude.home.view.HomeFragment
import br.com.policlinsaude.home.view.adapter.HomePageAdapter
import dagger.Module
import dagger.Provides

@Module
class HealthInsurancePhotoModule {

    @Provides
    fun providesHealthInsurancePhotoPresenter(navigator: HealthInsurancePhotoNavigator,
                                              getHealthInsurancePhotoUseCase: GetHealthInsurancePhotoUseCase,
                                              view: HealthInsurancePhotoView)
            : HealthInsurancePhotoPresenter = HealthInsurancePhotoPresenterImpl(navigator = navigator,
            getHealthInsurancePhotoUseCase = getHealthInsurancePhotoUseCase, view = view)

    @Provides
    fun provideHealthInsurancePhotoNavigator(activity: HealthInsurancePhotoActivity)
            : HealthInsurancePhotoNavigator = HealthInsurancePhotoNavigatorImpl(activity)

    @Provides
    fun provideHealthInsurancePhotoView(activity: HealthInsurancePhotoActivity)
            : HealthInsurancePhotoView = activity

    @Provides
    fun provideGetHealthInsurancePhotoUseCase(repository: Repository)
            : GetHealthInsurancePhotoUseCase = GetHealthInsurancePhotoUseCase(repository = repository)

    @Provides
    fun providePhotoPageAdapter(activity: HealthInsurancePhotoActivity): PhotoPageAdapter = PhotoPageAdapter(activity.applicationContext)


}




