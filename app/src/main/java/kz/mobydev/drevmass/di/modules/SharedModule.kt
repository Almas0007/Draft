package kz.mobydev.drevmass.di.modules

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import kz.mobydev.drevmass.data.preferences.PreferencesDataSource
import kz.mobydev.drevmass.data.preferences.PreferencesDataSourceImpl


@Module
class SharedModule {

    @Provides
    @Singleton
    fun providePreferences(app: Application): PreferencesDataSource {
        return PreferencesDataSourceImpl(app)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(app: Application): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(app)
    }
}