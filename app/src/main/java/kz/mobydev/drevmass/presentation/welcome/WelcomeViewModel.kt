package kz.mobydev.drevmass.presentation.welcome

import androidx.lifecycle.ViewModel
import javax.inject.Inject
import kz.mobydev.drevmass.repository.AppRepositoryImpl

class WelcomeViewModel @Inject constructor(
    private val repositoryImpl: AppRepositoryImpl
) : ViewModel() {


}