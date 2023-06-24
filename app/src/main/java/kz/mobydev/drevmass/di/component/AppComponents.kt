package kz.mobydev.drevmass.di.component

import android.content.Context
import android.content.SharedPreferences
import dagger.Component
import javax.inject.Singleton
import kz.mobydev.drevmass.MainActivity
import kz.mobydev.drevmass.data.local.AppDao
import kz.mobydev.drevmass.data.local.AppDb
import kz.mobydev.drevmass.data.remote.RemoteDataSource
import kz.mobydev.drevmass.data.remote.RemoteDataSourceImpl
import kz.mobydev.drevmass.di.modules.AppModule
import kz.mobydev.drevmass.di.modules.CoroutinesModule
import kz.mobydev.drevmass.di.modules.NetworkModule
import kz.mobydev.drevmass.di.modules.RepositoryModule
import kz.mobydev.drevmass.di.modules.SharedModule
import kz.mobydev.drevmass.di.modules.StorageModule
import kz.mobydev.drevmass.presentation.product.ProductFragment
import kz.mobydev.drevmass.presentation.additional.AdditionalFragment
import kz.mobydev.drevmass.presentation.favorite.FavoriteFragment
import kz.mobydev.drevmass.presentation.info.InfoFragment
import kz.mobydev.drevmass.presentation.lesssons.LessonsFragment
import kz.mobydev.drevmass.presentation.login.LogInFragment
import kz.mobydev.drevmass.presentation.recover.RecoverFragment
import kz.mobydev.drevmass.presentation.regin.RegInFragment
import kz.mobydev.drevmass.presentation.splash.SplashFragment
import kz.mobydev.drevmass.presentation.welcome.WelcomeFragment
import retrofit2.Retrofit

@Singleton
@Component(
    modules = [
        AppModule::class,
        NetworkModule::class,
        RepositoryModule::class,
        CoroutinesModule::class,
        StorageModule::class
    ]
)
interface AppComponents {
    fun context(): Context

    fun retrofit(): Retrofit

    fun appDao(): AppDao

    fun appDatabase(): AppDb

    fun inject(mainActivity: MainActivity)
    fun inject(splashFragment: SplashFragment)
    fun inject(welcomeFragment: WelcomeFragment)
    fun inject(regInFragment: RegInFragment)
    fun inject(logInFragment: LogInFragment)
    fun inject(recoverFragment: RecoverFragment)
    fun inject(lessonsFragment: LessonsFragment)
    fun inject(infoFragment: InfoFragment)
    fun inject(favoriteFragment: FavoriteFragment)
    fun inject(additionalFragment: AdditionalFragment)
    fun inject(productFragment: ProductFragment)


}