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

    var selectActivity = MutableLiveData<Int>()

    var selectMale = MutableLiveData<Boolean>()

    var selectHeight = MutableLiveData<Int>()

    var selectWeight = MutableLiveData<Double>()

    var selectBirthDay = MutableLiveData<String>()

    var selectTime = MutableLiveData<String>()

    var selectDay = MutableLiveData<List<Boolean>>()

    var DayPn = MutableLiveData<Boolean>()
    var DayVt = MutableLiveData<Boolean>()
    var DaySr = MutableLiveData<Boolean>()
    var DayCht = MutableLiveData<Boolean>()
    var DayPt = MutableLiveData<Boolean>()
    var DaySb = MutableLiveData<Boolean>()
    var DayVs = MutableLiveData<Boolean>()

    var selectDayList = MutableLiveData<List<Boolean>>()

    private var _errorMessage = MutableLiveData<String>()
    var errorMessage: LiveData<String> = _errorMessage

    fun getDayList(): LiveData<List<Boolean>> {
        val list = listOf<Boolean>(
            DayPn.value?:false,
            DayVt.value?:false,
            DaySr.value?:false,
            DayCht.value?:false,
            DayPt.value?:false,
            DaySb.value?:false,
            DayVs.value?:false
        )
        selectDayList.postValue(list)
        return selectDayList
    }




    fun updateUserInfo(token: String, name:String, email: String, password:String) {
        val userRequest = UserInfoPostRequest(
            name = name,
            email = email,
            password = password,
            passwordConfirmation = password,
            information = UserInfoPostRequest.Information(
                activity = selectActivity.value?:0,
                birth = selectBirthDay.value?:"2000-01-01",
                gender = selectMale.value?:true ,
                height = selectHeight.value?:0,
                weight = selectWeight.value?:0.0,
            )
        )
        viewModelScope.launch {
            try {
                when (val response = repositoryImpl.updateUserInformationApi(token, userRequest) ) {
                    is ResultData.Success -> {
                        _userInfoAfterUpdate.postValue(response.data)
                        _login.postValue(true)
                        Log.d("AAA", "Success update post" + response.data.toString())
                    }
                    is ResultData.Error -> {
                        _errorMessage.postValue(response.exception.toString())
                    }

                }
            } catch (e: Exception) {
                _errorMessage.postValue(e.toString())
            }
        }
    }

}