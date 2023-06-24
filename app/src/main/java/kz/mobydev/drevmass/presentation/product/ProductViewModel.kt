package kz.mobydev.drevmass.presentation.product

import androidx.lifecycle.ViewModel
import javax.inject.Inject
import kz.mobydev.drevmass.repository.AppRepositoryImpl

class ProductViewModel @Inject constructor(
    private val repositoryImpl: AppRepositoryImpl
) : ViewModel() {

}