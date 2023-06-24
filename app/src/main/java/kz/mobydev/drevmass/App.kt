package kz.mobydev.drevmass

import android.app.Application
import com.facebook.stetho.BuildConfig
import com.facebook.stetho.Stetho
import kz.mobydev.drevmass.di.component.AppComponents
import kz.mobydev.drevmass.di.component.DaggerAppComponents

import kz.mobydev.drevmass.di.modules.AppModule

import kz.mobydev.drevmass.di.modules.StorageModule
import kz.mobydev.drevmass.utils.InternetUtil
import timber.log.Timber

class App: Application() {
    companion object {
        lateinit var appComponents: AppComponents
    }
    override fun onCreate() {
        super.onCreate()
        appComponents = initDagger(this)
        initStetho()
        InternetUtil.init(this)
    }

    private fun initDagger(app: App): AppComponents =
        DaggerAppComponents.builder()
            .appModule(AppModule(app))
            .storageModule(StorageModule(app))
            .build()

    private fun initStetho() {
        if (BuildConfig.DEBUG)
            Stetho.initializeWithDefaults(this)
    }
    private fun initTimber() {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}
