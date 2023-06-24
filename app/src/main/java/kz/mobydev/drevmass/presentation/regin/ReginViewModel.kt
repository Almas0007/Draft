package kz.mobydev.drevmass.presentation.regin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import javax.inject.Inject
import kotlinx.coroutines.launch
import kz.mobydev.drevmass.model.User
import kz.mobydev.drevmass.model.common.ResultData
import kz.mobydev.drevmass.repository.AppRepositoryImpl

class ReginViewModel @Inject constructor(
    private val repositoryImpl: AppRepositoryImpl
) : ViewModel() {

    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    private var _login = MutableLiveData<User>()
    val login: LiveData<User> = _login

    private var _errorMessage = MutableLiveData<String>()
    var errorMessage: LiveData<String> = _errorMessage

    private var _errorMessageName = MutableLiveData<Boolean>()
    var errorMessageName: LiveData<Boolean> = _errorMessageName

    private var _errorMessageEmail = MutableLiveData<Boolean>()
    var errorMessageEmail: LiveData<Boolean> = _errorMessageEmail

    private var _errorMessagePassword = MutableLiveData<Boolean>()
    var errorMessagePassword: LiveData<Boolean> = _errorMessagePassword

    private var _errorMessageConfirmPassword = MutableLiveData<Boolean>()
    var errorMessageConfirmPassword: LiveData<Boolean> = _errorMessageConfirmPassword

    fun validationInputValue(name: String, email: String, password: String, password_confirmation: String): Boolean {
        return when {
            !emailCheck(email) || !passwordCheck(password, password_confirmation) || !nameCheck(name) -> {
                false
            }
            else -> {
                getUserRegin(name, email, password, password_confirmation)
                _errorMessageName.postValue(true)
                _errorMessageEmail.postValue(true)
                _errorMessagePassword.postValue(true)
                _errorMessageConfirmPassword.postValue(true)
                true
            }
        }
    }

     private fun getUserRegin(
        name: String,
        email: String,
        password: String,
        password_confirmation: String
    ) {
        viewModelScope.launch {
            try {
                when (val response = repositoryImpl.getRegisterApi(
                    name = name,
                    email = email,
                    password = password,
                    password_confirmation = password_confirmation
                )) {
                    is ResultData.Success-> {
                    _login.postValue(response.data)
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


    /** Check validation Data aat User **/

    private fun emailCheck(email: String):Boolean{
        return when {
            email.isEmpty() -> {
                _errorMessageEmail.postValue(false)
                false
            }

            !email.trim().matches(emailPattern.toRegex()) -> {
                _errorMessageEmail.postValue(false)
                false
            }

            else -> {
                _errorMessageEmail.postValue(true)
                true
            }
        }
    }
    private fun passwordCheck(password: String, password_confirmation: String):Boolean{
        return when{
            password.isEmpty() -> {
                _errorMessagePassword.postValue(false)
                false
            }
            password_confirmation.isEmpty()->{
                _errorMessageConfirmPassword.postValue(false)
                false
            }
            password_confirmation.isEmpty() && password.isEmpty()->{
                _errorMessagePassword.postValue(false)
                _errorMessageConfirmPassword.postValue(false)
                false
            }
            password.trim().length < 8 -> {
                _errorMessagePassword.postValue(false)
                false
            }
            password_confirmation.trim().length < 8 -> {
                _errorMessagePassword.postValue(false)
                false
            }

            password != password_confirmation ->{
                _errorMessagePassword.postValue(false)
                _errorMessageConfirmPassword.postValue(false)
                false
            }
            else -> {
                _errorMessagePassword.postValue(true)
                true
            }
        }
    }

    private fun nameCheck(name: String):Boolean{
        return when{
            name.isEmpty() -> {
                _errorMessagePassword.postValue(false)
                false
            }
            else -> {
                _errorMessagePassword.postValue(true)
                true
            }
        }
    }


}