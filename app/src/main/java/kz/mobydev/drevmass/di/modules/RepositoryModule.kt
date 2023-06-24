package kz.mobydev.drevmass.di.modules

import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kz.mobydev.drevmass.api.ApiService
import kz.mobydev.drevmass.data.local.AppDao
import kz.mobydev.drevmass.data.preferences.PreferencesDataSource
import kz.mobydev.drevmass.data.preferences.PreferencesDataSourceImpl
import kz.mobydev.drevmass.data.remote.RemoteDataSourceImpl
import kz.mobydev.drevmass.di.IoDispatcher
import kz.mobydev.drevmass.repository.AppRepositoryImpl

@Module
class RepositoryModule {


    @Provides
    @Singleton
    fun provideAppRepository(
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
        api: ApiService,
        appDao: AppDao
    ): AppRepositoryImpl {
        val userDataSource = RemoteDataSourceImpl(api, ioDispatcher)
        return AppRepositoryImpl(userDataSource, appDao,ioDispatcher)
    }
}