package br.com.data.di

import android.content.Context
import br.com.data.RepositoryImpl
import br.com.data.datasource.networking.NetworkingDatasource
import br.com.data.datasource.networking.NetworkingDatasourceImpl
import br.com.data.datasource.networking.rest.NetworkingService
import br.com.data.datasource.preferences.PreferencesDatasource
import br.com.data.datasource.preferences.PreferencesDatasourceImpl
import br.com.data.datasource.realm.RealmDatasource
import br.com.data.datasource.realm.RealmDatasourceImpl
import br.com.domain.repository.Repository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun providesRepository(networkingDatasource: NetworkingDatasource,
                           realmDatasource: RealmDatasource,
                           preferencesDatasource: PreferencesDatasource): Repository
            = RepositoryImpl(networkingDatasource = networkingDatasource,
            realmDatasource = realmDatasource,
            preferencesDatasource = preferencesDatasource)

    @Provides
    fun providesRealmDatasource(): RealmDatasource = RealmDatasourceImpl()

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