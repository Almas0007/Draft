package kz.mobydev.drevmass.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import javax.inject.Inject
import kotlinx.coroutines.launch
import kz.mobydev.drevmass.model.Products
import kz.mobydev.drevmass.model.UserInfoGet
import kz.mobydev.drevmass.model.common.ResultData
import kz.mobydev.drevmass.repository.AppRepositoryImpl

class ProfileViewModel  @Inject constructor(
    private val repositoryImpl: AppRepositoryImpl
) : ViewModel() {

    private var _userInfo = MutableLiveData<UserInfoGet>()
    var userInfo: LiveData<UserInfoGet> = _userInfo



    private var _errorMessage = MutableLiveData<String>()
    var errorMessage: LiveData<String> = _errorMessage

    fun getUser(token:String) {
        viewModelScope.launch {
            try {
                when (val response = repositoryImpl.getUserInformationApi(token) ){
                    is ResultData.Success-> {
                        _userInfo.postValue(response.data)
                    }
                    is ResultData.Error -> {
                        _errorMessage.postValue(response.exception.message)
                    }
                }
            } catch (e: Exception) {
                _errorMessage.postValue(e.toString())
            }

        }
    }


}