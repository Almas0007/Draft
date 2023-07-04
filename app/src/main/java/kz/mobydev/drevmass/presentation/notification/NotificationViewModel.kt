package kz.mobydev.drevmass.presentation.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import javax.inject.Inject
import kotlinx.coroutines.launch
import kz.mobydev.drevmass.model.UserInfoGet
import kz.mobydev.drevmass.model.common.ResultData
import kz.mobydev.drevmass.model.day.DayPostResponse
import kz.mobydev.drevmass.model.day.DaysPostRequest
import kz.mobydev.drevmass.repository.AppRepositoryImpl

class NotificationViewModel @Inject constructor(
    private val repositoryImpl: AppRepositoryImpl
) : ViewModel() {

    private var _user = MutableLiveData<UserInfoGet>()
    var user :LiveData<UserInfoGet> = _user

    private var _errorMessage = MutableLiveData<String>()
    var errorMessage: LiveData<String> = _errorMessage


    private var _day = MutableLiveData<DayPostResponse>()
    var day :LiveData<DayPostResponse> = _day

    fun setNotification(token: String, day:DaysPostRequest){
        viewModelScope.launch {
            try {
                when (val response = repositoryImpl.setDay(token,day) ){
                    is ResultData.Success-> {
                        _day.postValue(response.data)
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