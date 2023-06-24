package kz.mobydev.drevmass.di.modules

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import kz.mobydev.drevmass.data.local.AppDao
import kz.mobydev.drevmass.data.local.AppDb

@Module
class StorageModule(private val application: Application) {
    private var appDb: AppDb =
        Room.databaseBuilder(application, AppDb::class.java, "app-db").build()

    @Singleton
    @Provides
    fun provideAppDb(): AppDb {
        return appDb
    }

    @Singleton
    @Provides
    fun provideAppDao(demoDatabase:AppDb): AppDao {
        return demoDatabase.appDao()
    }

}