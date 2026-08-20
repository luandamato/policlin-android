package br.com.policlinsaude.otherFeatures.di

import br.com.policlinsaude.otherFeatures.data.repositories.*
import br.com.policlinsaude.otherFeatures.features.Token.ui.TokenViewModel
import br.com.policlinsaude.otherFeatures.features.coparticipation.data.repositories.CoParticipationRepository
import br.com.policlinsaude.otherFeatures.features.coparticipation.data.repositories.CoParticipationRepositoryImpl
import br.com.policlinsaude.otherFeatures.features.coparticipation.ui.viewmodels.CoParticipationViewModel
import br.com.policlinsaude.otherFeatures.features.deleteUser.ui.ViewModel.DeleteUserViewModel
import br.com.policlinsaude.otherFeatures.features.extractor.ui.viewmodels.FactorExtractorViewModel
import br.com.policlinsaude.otherFeatures.features.guidAuthorizer.ui.viewmodels.GuideAuthorizerViewModel
import br.com.policlinsaude.otherFeatures.features.incometax.ui.viewmodels.IncomeTaxViewModel
import br.com.policlinsaude.otherFeatures.features.incometax.ui.viewmodels.ScheduleCentralViewModel
import br.com.policlinsaude.otherFeatures.features.notifications.ui.viewmodels.NotificationViewModel
import br.com.policlinsaude.otherFeatures.features.tickets.ui.viewmodels.TicketViewModel
import br.com.policlinsaude.otherFeatures.utils.SharedPreferences
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModules = module {

    single { SharedPreferences(get()) }

    single<TicketRepository> { TicketRepositoryImpl() }
    single<FactorExtractorRepository> { FactorExtractorRepositoryImpl() }
    single<CoParticipationRepository> { CoParticipationRepositoryImpl() }
    single<NotificationRepository> { NotificationRepositoryImpl() }
    single<GuideRepository> { GuideRepositoryImpl() }

    viewModel { TicketViewModel(get(), get()) }
    viewModel { CoParticipationViewModel(get(), get()) }
    viewModel { FactorExtractorViewModel(get(), get()) }
    viewModel { NotificationViewModel(get(), get()) }
    viewModel { IncomeTaxViewModel(get(), get()) }
    viewModel { GuideAuthorizerViewModel(get(), get()) }
    viewModel { ScheduleCentralViewModel(get(), get()) }
    viewModel { TokenViewModel(get(), get()) }
    viewModel { DeleteUserViewModel(get(), get()) }
}
