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


      fun regInUser(
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


}