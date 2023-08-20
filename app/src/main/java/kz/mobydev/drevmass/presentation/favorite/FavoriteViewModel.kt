package kz.mobydev.drevmass.presentation.favorite

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import javax.inject.Inject
import kotlinx.coroutines.launch
import kz.mobydev.drevmass.model.Lesson
import kz.mobydev.drevmass.model.common.ResultData
import kz.mobydev.drevmass.repository.AppRepositoryImpl

class FavoriteViewModel @Inject constructor(
    private val repositoryImpl: AppRepositoryImpl
) : ViewModel() {

    private var _favoriteList = MutableLiveData<List<Lesson>>()
    var favoriteList: LiveData<List<Lesson>> = _favoriteList


    private var _errorMessage = MutableLiveData<String>()
    var errorMessage: LiveData<String> = _errorMessage

    private var _removeMessage = MutableLiveData<Boolean>()
    var removeMessage: LiveData<Boolean> = _removeMessage

    fun getFavorite(token: String) {
        viewModelScope.launch {
            try {
                when (val response = repositoryImpl.getFavoriteAPI(token)) {
                    is ResultData.Success -> {
                        _favoriteList.postValue(response.data)
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

    fun actionFavorite(token: String, id: Int, action: String) {
        viewModelScope.launch {
            try {
                when (val response = repositoryImpl.actionFavoriteAPI(token, id, action)) {
                    is ResultData.Success -> {
                        if (action == "remove") {
                            _removeMessage.postValue(true)
                        }
                    }

                    is ResultData.Error -> {
                        Log.d("AAA", response.exception.message.toString())
                        _errorMessage.postValue(response.exception.message)
                    }
                }
            } catch (e: Exception) {
                Log.d("AAA", e.message.toString())
                _errorMessage.postValue(e.toString())
            }

        }
    }

}