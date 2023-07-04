package kz.mobydev.drevmass.di.component

import android.content.Context
import dagger.Component
import javax.inject.Singleton
import kz.mobydev.drevmass.MainActivity
import kz.mobydev.drevmass.data.local.AppDao
import kz.mobydev.drevmass.data.local.AppDb
import kz.mobydev.drevmass.di.modules.AppModule
import kz.mobydev.drevmass.di.modules.CoroutinesModule
import kz.mobydev.drevmass.di.modules.NetworkModule
import kz.mobydev.drevmass.di.modules.RepositoryModule
import kz.mobydev.drevmass.di.modules.StorageModule
import kz.mobydev.drevmass.presentation.product.ProductFragment
import kz.mobydev.drevmass.presentation.lesssons.about.LessonAboutFragment
import kz.mobydev.drevmass.presentation.favorite.FavoriteFragment
import kz.mobydev.drevmass.presentation.info.InfoFragment
import kz.mobydev.drevmass.presentation.lesssons.LessonsFragment
import kz.mobydev.drevmass.presentation.login.LogInFragment
import kz.mobydev.drevmass.presentation.notification.NotificationFragment
import kz.mobydev.drevmass.presentation.product.about.AboutProductFragment
import kz.mobydev.drevmass.presentation.profile.ProfileFragment
import kz.mobydev.drevmass.presentation.profile.aboutdrev.AboutFragment
import kz.mobydev.drevmass.presentation.profile.callback.CallbackFragment
import kz.mobydev.drevmass.presentation.profile.doc.DocumentationFragment
import kz.mobydev.drevmass.presentation.profile.user.UserFragment
import kz.mobydev.drevmass.presentation.recover.RecoverFragment
import kz.mobydev.drevmass.presentation.regin.RegInFragment
import kz.mobydev.drevmass.presentation.reset_password.ResetPasswordFragment
import kz.mobydev.drevmass.presentation.splash.SplashFragment
import kz.mobydev.drevmass.presentation.video.VideoFragment
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
    fun inject(lessonAboutFragment: LessonAboutFragment)
    fun inject(productFragment: ProductFragment)
    fun inject(aboutProductFragment: AboutProductFragment)
    fun inject(profileFragment: ProfileFragment)
    fun inject(userFragment: UserFragment)
    fun inject(aboutFragment: AboutFragment)
    fun inject(callbackFragment: CallbackFragment)
    fun inject(documentationFragment: DocumentationFragment)
    fun inject(videoFragment: VideoFragment)
    fun inject(notificationFragment: NotificationFragment)
    fun inject(resetPasswordFragment: ResetPasswordFragment)

}