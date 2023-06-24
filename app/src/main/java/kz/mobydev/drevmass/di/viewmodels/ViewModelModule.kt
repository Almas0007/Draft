package kz.mobydev.drevmass.di.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kz.mobydev.drevmass.data.preferences.PreferencesDataSource
import kz.mobydev.drevmass.data.preferences.PreferencesDataSourceImpl
import kz.mobydev.drevmass.presentation.info.InfoViewModel
import kz.mobydev.drevmass.presentation.login.LoginViewModel
import kz.mobydev.drevmass.presentation.product.ProductViewModel
import kz.mobydev.drevmass.presentation.regin.ReginViewModel
import kz.mobydev.drevmass.presentation.welcome.WelcomeViewModel

@Module
abstract class ViewModelModule {
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
    abstract fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory
}