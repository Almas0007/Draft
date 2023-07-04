package kz.mobydev.drevmass.presentation.reset_password

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import javax.inject.Inject
import kotlinx.coroutines.launch
import kz.mobydev.drevmass.model.MessageResetPassword
import kz.mobydev.drevmass.model.User
import kz.mobydev.drevmass.model.common.ResultData
import kz.mobydev.drevmass.repository.AppRepositoryImpl

class ResetPasswordViewModel @Inject constructor(
    private val repositoryImpl: AppRepositoryImpl
) : ViewModel() {
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    private var _message = MutableLiveData<MessageResetPassword>()
    val message: LiveData<MessageResetPassword> = _message

    private var _errorMessage = MutableLiveData<String>()
    var errorMessage: LiveData<String> = _errorMessage

    private var _errorMessageLogin = MutableLiveData<Boolean>()
    var errorMessageLogin: LiveData<Boolean> = _errorMessageLogin

    private var _errorMessagePassword = MutableLiveData<Boolean>()
    var errorMessagePassword: LiveData<Boolean> = _errorMessagePassword




     fun emailCheck(email: String):Boolean{
        return when {
            email.isEmpty() -> {
                _errorMessageLogin.postValue(false)
                false
            }

            !email.trim().matches(emailPattern.toRegex()) -> {
                _errorMessageLogin.postValue(false)
                false
            }

            else -> {
                _errorMessageLogin.postValue(true)
                forgetPassword(email)
                true
            }
        }
    }


    private fun forgetPassword(email: String) {

        viewModelScope.launch {
            try {
                when (val response = repositoryImpl.forgetPassword(email)) {
                    is ResultData.Success-> {

                        _message.postValue(response.data)

                    }

                    is ResultData.Error -> {
                    }
                }
            }catch (e: Exception) {

            }

        }
    }

}