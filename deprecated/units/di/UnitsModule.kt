package br.com.policlinsaude.units.di

import br.com.domain.repository.Repository
import br.com.domain.usecase.GetUnitsUseCase
import br.com.policlinsaude.units.navigator.UnitsNavigator
import br.com.policlinsaude.units.navigator.UnitsNavigatorImpl
import br.com.policlinsaude.units.presenter.UnitsPresenter
import br.com.policlinsaude.units.presenter.UnitsPresenterImpl
import br.com.policlinsaude.units.view.UnitsActivity
import br.com.policlinsaude.units.view.UnitsView
import br.com.policlinsaude.units.view.adapter.UnitsPageAdapter
import dagger.Module
import dagger.Provides

@Module
class UnitsModule {

    @Provides
    fun providesMedicalGuideListPresenter(getUnitsUseCase: GetUnitsUseCase,
                                          view: UnitsActivity,
                                          navigator: UnitsNavigator)
            : UnitsPresenter = UnitsPresenterImpl(
            getUnitsUseCase = getUnitsUseCase, view = view, navigator = navigator)

    @Provides
    fun provideUnitsView(activity: UnitsActivity)
            : UnitsView = activity

    @Provides
    fun provideGetUnitsUseCase(repository: Repository)
            : GetUnitsUseCase = GetUnitsUseCase(repository = repository)

  /*  @Provides
    fun provideUnitsPageAdapter(activity: UnitsActivity): UnitsPageAdapter
            = UnitsPageAdapter(activity.supportFragmentManager)*/

    @Provides
    fun provideUnitsNavigator(activity: UnitsActivity)
            : UnitsNavigator = UnitsNavigatorImpl(activity)
}