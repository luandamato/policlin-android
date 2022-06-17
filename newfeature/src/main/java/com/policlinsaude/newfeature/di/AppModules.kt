package com.policlinsaude.newfeature.di

import com.policlinsaude.newfeature.data.repositories.TicketRepository
import com.policlinsaude.newfeature.data.repositories.TicketRepositoryImpl
import com.policlinsaude.newfeature.features.tickets.data.models.TicketDetail
import com.policlinsaude.newfeature.features.tickets.ui.viewmodels.TicketViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModules = module {

    single<TicketRepository> { TicketRepositoryImpl() }
    single { TicketViewModel(get()) }
}