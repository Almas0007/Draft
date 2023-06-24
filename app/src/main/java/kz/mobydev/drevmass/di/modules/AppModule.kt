package kz.mobydev.drevmass.di.modules

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import kz.mobydev.drevmass.di.viewmodels.ViewModelModule


@Module(includes = [ViewModelModule::class, SharedModule::class])
class AppModule(private val application:Application) {
    @Provides
    @Singleton
    fun provideApplication(): Application {
        return application
    }

    @Provides
    @Singleton
    fun provideContext(): Context {
        return application.applicationContext
    }
}