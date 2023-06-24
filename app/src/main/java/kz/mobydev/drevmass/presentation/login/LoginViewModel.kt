package kz.mobydev.drevmass.presentation.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import javax.inject.Inject
import kotlinx.coroutines.launch
import kz.mobydev.drevmass.model.User
import kz.mobydev.drevmass.model.common.ResultData
import kz.mobydev.drevmass.repository.AppRepositoryImpl

class LoginViewModel @Inject constructor(
    private val repositoryImpl: AppRepositoryImpl
) : ViewModel() {
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    private var _login = MutableLiveData<User>()
    val login: LiveData<User> = _login

    private var _errorMessage = MutableLiveData<String>()
    var errorMessage: LiveData<String> = _errorMessage

    private var _errorMessageLogin = MutableLiveData<Boolean>()
    var errorMessageLogin: LiveData<Boolean> = _errorMessageLogin

    private var _errorMessagePassword = MutableLiveData<Boolean>()
    var errorMessagePassword: LiveData<Boolean> = _errorMessagePassword

    private var _showLoading = MutableLiveData<Boolean>()
    var showLoading: LiveData<Boolean> = _showLoading

    private var _checkBtn = MutableLiveData<Boolean>()
    var buttonCheckChangeTint: LiveData<Boolean> = _checkBtn

    var loginChange = MutableLiveData<String>()
    var passwordChange = MutableLiveData<String>()

    init {
        buttonBackgroundTintChange()
    }
    private fun buttonBackgroundTintChange() {
        Log.d("AAA",loginChange.value.toString())
        if ((loginChange.value.isNullOrEmpty()) && passwordChange.value.isNullOrEmpty() ){
            _checkBtn.postValue(false)
        } else {
            _checkBtn.postValue(true)
        }
    }

    fun validationInputValue(email: String, password: String): Boolean {
        return when {
            !emailCheck(email) && passwordCheck(password) -> {
                false
            }
            !passwordCheck(password)&& emailCheck(email) -> {
                false
            }
            !emailCheck(email) && !passwordCheck(password) -> {
                _errorMessageLogin.postValue(false)
                _errorMessagePassword.postValue(false)
                false
            }
            else -> {
                getUserLogin(email, password)
                _errorMessageLogin.postValue(true)
                _errorMessagePassword.postValue(true)
                true
            }
        }
    }

    private fun emailCheck(email: String):Boolean{
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
                true
            }
        }
    }
    private fun passwordCheck(password: String):Boolean{
        return when{
            password.isEmpty() -> {
                _errorMessagePassword.postValue(false)
                false
            }
            password.trim().length < 8 -> {
                _errorMessagePassword.postValue(false)
                false
            }
            else -> {
                _errorMessagePassword.postValue(true)
                true
            }
        }
    }

    private fun getUserLogin(email: String, password: String) {
        _showLoading.postValue(true)
        viewModelScope.launch {
            try {
                when (val response = repositoryImpl.getLoginApi(email, password)) {
                    is ResultData.Success-> {
                        _showLoading.postValue(false)
                        _login.postValue(response.data)

                    }

                    is ResultData.Error -> {
                        _showLoading.postValue(false)
                        _errorMessage.postValue(response.exception.toString())
                    }
                }
            }catch (e: Exception) {
                _showLoading.postValue(false)
                _errorMessage.postValue(e.toString())
            }

        }
    }
}