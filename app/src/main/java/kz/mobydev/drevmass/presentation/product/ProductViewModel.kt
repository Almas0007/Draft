package kz.mobydev.drevmass.presentation.product

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import javax.inject.Inject
import kotlinx.coroutines.launch
import kz.mobydev.drevmass.model.Products
import kz.mobydev.drevmass.model.common.ResultData
import kz.mobydev.drevmass.repository.AppRepositoryImpl

class ProductViewModel @Inject constructor(
    private val repositoryImpl: AppRepositoryImpl
) : ViewModel() {

    private var _products = MutableLiveData<Products>()
    var products: LiveData<Products> = _products


    private var _errorMessage = MutableLiveData<String>()
    var errorMessage: LiveData<String> = _errorMessage

    fun getProducts(token:String) {
        viewModelScope.launch {
            try {
                when (val response = repositoryImpl.getProducts(token) ){
                    is ResultData.Success-> {
                       _products.postValue(response.data)
                        Log.d("TAG", "Vse pashet: ${response.data}")
                    }
                    is ResultData.Error -> {
                        Log.d("TAG", "getProducts: ${response.exception.message}")
                        _errorMessage.postValue(response.exception.message)
                    }
                }
            } catch (e: Exception) {
                Log.d("TAG", "getProductsERRRRR: ${e.toString()}}")
                _errorMessage.postValue(e.toString())
            }

        }
    }

}