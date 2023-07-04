package kz.mobydev.drevmass.presentation.profile.user

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import javax.inject.Inject
import kotlinx.coroutines.launch
import kz.mobydev.drevmass.model.UserInfoGet
import kz.mobydev.drevmass.model.UserInfoPostRequest
import kz.mobydev.drevmass.model.UserInfoPostResponse
import kz.mobydev.drevmass.model.common.ResultData
import kz.mobydev.drevmass.model.common.UserInfoPost
import kz.mobydev.drevmass.repository.AppRepositoryImpl

class UserViewModel @Inject constructor(
    private val app: Application,
    private val repositoryImpl: AppRepositoryImpl
) : AndroidViewModel(app) {

    private var _userInfo = MutableLiveData<UserInfoGet>()
    var userInfo: LiveData<UserInfoGet> = _userInfo

    private var _userPostResponse = MutableLiveData<UserInfoPostResponse>()
    var userInfoResponse: LiveData<UserInfoPostResponse> = _userPostResponse

    var click = MutableLiveData<Boolean>()

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

    fun updateUser(token: String, userInfoPostRequest: UserInfoPostRequest){
        viewModelScope.launch {
            try {
                when (val response = repositoryImpl.updateUserInformationApi(token, userInfoPostRequest) ){
                    is ResultData.Success-> {
                        _userPostResponse.postValue(response.data)
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