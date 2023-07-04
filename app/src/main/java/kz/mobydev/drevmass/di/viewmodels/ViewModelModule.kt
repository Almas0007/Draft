package kz.mobydev.drevmass.di.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kz.mobydev.drevmass.ActivityViewModel
import kz.mobydev.drevmass.data.preferences.PreferencesDataSource
import kz.mobydev.drevmass.data.preferences.PreferencesDataSourceImpl
import kz.mobydev.drevmass.presentation.favorite.FavoriteViewModel
import kz.mobydev.drevmass.presentation.info.InfoViewModel
import kz.mobydev.drevmass.presentation.lesssons.LessonsViewModel
import kz.mobydev.drevmass.presentation.lesssons.about.LessonAboutViewModel
import kz.mobydev.drevmass.presentation.login.LoginViewModel
import kz.mobydev.drevmass.presentation.notification.NotificationViewModel
import kz.mobydev.drevmass.presentation.product.ProductViewModel
import kz.mobydev.drevmass.presentation.product.about.AboutProductViewModel
import kz.mobydev.drevmass.presentation.profile.ProfileViewModel
import kz.mobydev.drevmass.presentation.profile.callback.CallbackViewModel
import kz.mobydev.drevmass.presentation.profile.user.UserViewModel
import kz.mobydev.drevmass.presentation.regin.ReginViewModel
import kz.mobydev.drevmass.presentation.reset_password.ResetPasswordViewModel
import kz.mobydev.drevmass.presentation.welcome.WelcomeViewModel

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ActivityViewModel::class)
    abstract fun bindActivityVM(activityViewModel: ActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginVM(LoginViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ReginViewModel::class)
    abstract fun bindReginVM(ReginViewModel: ReginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WelcomeViewModel::class)
    abstract fun bindWelcomeVM(ReginViewModel: WelcomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(InfoViewModel::class)
    abstract fun bindInfoVM(InfoViewModel: InfoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProductViewModel::class)
    abstract fun bindProductVM(ProductViewModel: ProductViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AboutProductViewModel::class)
    abstract fun bindAboutProductVM(AboutProductViewModel: AboutProductViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(LessonsViewModel::class)
    abstract fun bindLessonsVM(lessonsViewModel: LessonsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LessonAboutViewModel::class)
    abstract fun bindAboutLessonVM(lessonsAboutViewModel: LessonAboutViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavoriteViewModel::class)
    abstract fun bindFavoriteVM(favoriteViewModel: FavoriteViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindProfileVM(profileViewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel::class)
    abstract fun bindUserVM(userViewModel: UserViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CallbackViewModel::class)
    abstract fun bindCallbackVM(callbackViewModel: CallbackViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NotificationViewModel::class)
    abstract fun bindNotificationVM(notificationViewModel: NotificationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ResetPasswordViewModel::class)
    abstract fun bindResetPasswordVM(resetPasswordViewModel: ResetPasswordViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory
}