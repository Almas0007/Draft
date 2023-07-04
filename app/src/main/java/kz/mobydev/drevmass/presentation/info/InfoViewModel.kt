package kz.mobydev.drevmass.presentation.info

import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
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
import kz.mobydev.drevmass.model.day.DayPostResponse
import kz.mobydev.drevmass.model.day.DaysPostRequest
import kz.mobydev.drevmass.repository.AppRepositoryImpl


class InfoViewModel @Inject constructor(
    private val repositoryImpl: AppRepositoryImpl
) : ViewModel() {


    private var _userInfo = MutableLiveData<UserInfoGet>()
    val userInfo: LiveData<UserInfoGet> = _userInfo

    private var _userInfoAfterUpdate = MutableLiveData<UserInfoPostResponse>()
    val userInfoAfterUpdate: LiveData<UserInfoPostResponse> = _userInfoAfterUpdate

    private var _login = MutableLiveData<Boolean>()
    val login: LiveData<Boolean> = _login

    private var _user = MutableLiveData<UserInfoGet>()
    var user :LiveData<UserInfoGet> = _user

    private var _errorMessage = MutableLiveData<String>()
    var errorMessage: LiveData<String> = _errorMessage


    private var _day = MutableLiveData<DayPostResponse>()
    var day :LiveData<DayPostResponse> = _day

    private var _userPostResponse = MutableLiveData<UserInfoPostResponse>()
    var userInfoResponse: LiveData<UserInfoPostResponse> = _userPostResponse


    fun setNotification(token: String, day: DaysPostRequest){
        viewModelScope.launch {
            try {
                when (val response = repositoryImpl.setDay(token,day) ){
                    is ResultData.Success-> {
                        _day.postValue(response.data)
                    }
                    is ResultData.Error -> {

//                        _errorMessage.postValue(response.exception.message)
                    }
                }
            } catch (e: Exception) {

//                _errorMessage.postValue(e.toString())
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
    fun getUserInformation(token:String) {
        viewModelScope.launch {
            try {
                when (val response = repositoryImpl.getUserInformationApi(token) ){
                    is ResultData.Success-> {
                        _user.postValue(response.data)
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