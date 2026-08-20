package br.com.policlinsaude.data.di

import android.content.Context
import br.com.policlinsaude.data.RepositoryImpl
import br.com.policlinsaude.data.datasource.networking.NetworkingDatasource
import br.com.policlinsaude.data.datasource.networking.NetworkingDatasourceImpl
import br.com.policlinsaude.data.datasource.networking.rest.NetworkingService
import br.com.policlinsaude.data.datasource.preferences.PreferencesDatasource
import br.com.policlinsaude.data.datasource.preferences.PreferencesDatasourceImpl
import br.com.policlinsaude.domain.repository.Repository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun providesRepository(networkingDatasource: NetworkingDatasource,
                           preferencesDatasource: PreferencesDatasource): Repository
            = RepositoryImpl(networkingDatasource = networkingDatasource,
            preferencesDatasource = preferencesDatasource)

    @Provides
    fun providesPreferencesDatasource(context: Context): PreferencesDatasource
            = PreferencesDatasourceImpl(context)

    @Provides
    fun providesNetworkingDatasource(networkingService: NetworkingService): NetworkingDatasource
            = NetworkingDatasourceImpl(networkingService = networkingService)

    @Provides
    fun providesNetworkingService(retrofit: Retrofit): NetworkingService
            = retrofit.create(NetworkingService::class.java)
}
