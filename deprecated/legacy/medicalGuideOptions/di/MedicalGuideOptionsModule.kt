package br.com.policlinsaude.ui.legacy.medicalGuideOptions.di

import br.com.domain.repository.Repository
import br.com.domain.usecase.GetBooleanPreferenceUseCase
import br.com.domain.usecase.GetCurrentPersonUseCase
import br.com.domain.usecase.GetMedicalGuideOptionsUseCase
import br.com.domain.usecase.SetLocationPreferenceUseCase
import br.com.policlinsaude.medicalGuideOptions.navigator.MedicalGuideOptionsNavigator
import br.com.policlinsaude.medicalGuideOptions.navigator.MedicalGuideOptionsNavigatorImpl
import br.com.policlinsaude.medicalGuideOptions.presenter.MedicalGuideOptionsPresenter
import br.com.policlinsaude.medicalGuideOptions.presenter.MedicalGuideOptionsPresenterImpl
import br.com.policlinsaude.medicalGuideOptions.view.MedicalGuideOptionsActivity
import br.com.policlinsaude.medicalGuideOptions.view.MedicalGuideOptionsView
import dagger.Module
import dagger.Provides

@Module
class MedicalGuideOptionsModule {

    @Provides
    fun providesMedicalGuideOptionsPresenter(navigator: MedicalGuideOptionsNavigator,
                                             getMedicalGuideOptionsUseCase: GetMedicalGuideOptionsUseCase,
                                             view: MedicalGuideOptionsView,
                                             getCurrentPersonUseCase: GetCurrentPersonUseCase,
                                             getBooleanPreferenceUseCase: GetBooleanPreferenceUseCase,
                                             setLocationPreferenceUseCase: SetLocationPreferenceUseCase)
            : MedicalGuideOptionsPresenter = MedicalGuideOptionsPresenterImpl(navigator = navigator,
            getMedicalGuideOptionsUseCase = getMedicalGuideOptionsUseCase, view = view,
            getCurrentPersonUseCase = getCurrentPersonUseCase,
            getBooleanPreferenceUseCase = getBooleanPreferenceUseCase,
            setLocationPreferenceUseCase = setLocationPreferenceUseCase)

    @Provides
    fun provideMedicalGuideOptionsNavigator(activity: MedicalGuideOptionsActivity)
            : MedicalGuideOptionsNavigator = MedicalGuideOptionsNavigatorImpl(activity)

    @Provides
    fun provideMedicalGuideOptionsView(activity: MedicalGuideOptionsActivity)
            : MedicalGuideOptionsView = activity

    @Provides
    fun provideGetMedicalGuideOptionsUseCase(repository: Repository)
            : GetMedicalGuideOptionsUseCase = GetMedicalGuideOptionsUseCase(repository = repository)

}