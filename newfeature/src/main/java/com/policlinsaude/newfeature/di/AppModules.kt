package com.policlinsaude.newfeature.di

import com.policlinsaude.newfeature.data.repositories.FactorExtractorRepository
import com.policlinsaude.newfeature.data.repositories.FactorExtractorRepositoryImpl
import com.policlinsaude.newfeature.data.repositories.TicketRepository
import com.policlinsaude.newfeature.data.repositories.TicketRepositoryImpl
import com.policlinsaude.newfeature.features.coparticipation.data.repositories.CoParticipationRepository
import com.policlinsaude.newfeature.features.coparticipation.data.repositories.CoParticipationRepositoryImpl
import com.policlinsaude.newfeature.features.coparticipation.ui.viewmodels.CoParticipationViewModel
import com.policlinsaude.newfeature.features.extractor.ui.viewmodels.FactorExtractorViewModel
import com.policlinsaude.newfeature.features.tickets.ui.viewmodels.TicketViewModel
import com.policlinsaude.newfeature.utils.SharedPreferences
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModules = module {

    single { SharedPreferences(get()) }

    single<TicketRepository> { TicketRepositoryImpl() }

    single<FactorExtractorRepository> { FactorExtractorRepositoryImpl() }

    single<CoParticipationRepository> { CoParticipationRepositoryImpl() }

    viewModel { TicketViewModel(get(), get()) }

    viewModel { CoParticipationViewModel(get(), get()) }

    viewModel { FactorExtractorViewModel(get(), get()) }
}