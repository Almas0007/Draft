package kz.mobydev.drevmass.presentation.profile.callback

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import javax.inject.Inject
import kotlinx.coroutines.launch
import kz.mobydev.drevmass.model.Support
import kz.mobydev.drevmass.model.UserInfoGet
import kz.mobydev.drevmass.model.common.ResultData
import kz.mobydev.drevmass.repository.AppRepositoryImpl

class CallbackViewModel  @Inject constructor(
    private val repositoryImpl: AppRepositoryImpl
) : ViewModel() {

     var _info = MutableLiveData<Support?>()



    private var _errorMessage = MutableLiveData<String>()
    var errorMessage: LiveData<String> = _errorMessage

    fun sendMessageForAdmin(token:String,message:String) {
        _info.postValue(null)
        viewModelScope.launch {
            try {
                when (val response = repositoryImpl.supportMessage(token,message) ){
                    is ResultData.Success-> {
                        _info.postValue(response.data)
                        Log.d("TAG", "sendMessageForAdmin: ${response.data}")
                    }
                    is ResultData.Error -> {
                        _errorMessage.postValue(response.exception.message)
                        Log.d("TAG", "sendMessageForAdminRSEERR: ${response.exception.message}")
                    }
                }
            } catch (e: Exception) {
                _errorMessage.postValue(e.toString())
                Log.d("TAG", "sendMessageForAdmin ERRROr: ${e.toString()}")
            }

        }
    }
}