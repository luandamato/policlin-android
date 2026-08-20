package br.com.policlinsaude.medicalGuideList.di

import br.com.domain.repository.Repository
import br.com.domain.usecase.GetMedicalGuideListUseCase
import br.com.policlinsaude.medicalGuideList.navigator.MedicalGuideListNavigator
import br.com.policlinsaude.medicalGuideList.navigator.MedicalGuideListNavigatorImpl
import br.com.policlinsaude.medicalGuideList.presenter.MedicalGuideListPresenter
import br.com.policlinsaude.medicalGuideList.presenter.MedicalGuideListPresenterImpl
import br.com.policlinsaude.medicalGuideList.view.MedicalGuideListActivity
import br.com.policlinsaude.medicalGuideList.view.MedicalGuideListView
import br.com.policlinsaude.medicalGuideList.view.adapter.MedicalGuideListAdapter
import dagger.Module
import dagger.Provides

@Module
class MedicalGuideListModule {

    @Provides
    fun providesMedicalGuideListPresenter(navigator: MedicalGuideListNavigator,
                                          getMedicalGuideListUseCase: GetMedicalGuideListUseCase,
                                             view: MedicalGuideListView)
            : MedicalGuideListPresenter = MedicalGuideListPresenterImpl(navigator = navigator,
            getMedicalGuideListUseCase = getMedicalGuideListUseCase, view = view)

    @Provides
    fun provideMedicalGuideOptionsNavigator(activity: MedicalGuideListActivity)
            : MedicalGuideListNavigator = MedicalGuideListNavigatorImpl(activity)

    @Provides
    fun provideMedicalGuideListView(activity: MedicalGuideListActivity)
            : MedicalGuideListView = activity

    @Provides
    fun provideGetMedicalGuideListUseCase(repository: Repository)
            : GetMedicalGuideListUseCase = GetMedicalGuideListUseCase(repository = repository)

}