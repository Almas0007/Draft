package kz.mobydev.drevmass.presentation.lesssons

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import javax.inject.Inject
import kotlinx.coroutines.launch
import kz.mobydev.drevmass.model.Lesson
import kz.mobydev.drevmass.model.Products
import kz.mobydev.drevmass.model.StatusModel
import kz.mobydev.drevmass.model.common.ResultData
import kz.mobydev.drevmass.repository.AppRepositoryImpl
import kz.mobydev.drevmass.utils.InternetUtil

class LessonsViewModel @Inject constructor(
    private val repositoryImpl: AppRepositoryImpl
) : ViewModel()  {


    private var _lessons = MutableLiveData<List<Lesson>>()
    var lessons: LiveData<List<Lesson>> = _lessons


    private var _lessonsFavorite = MutableLiveData<List<Lesson>>()
    var lessonsFavorite: LiveData<List<Lesson>> = _lessonsFavorite

    private var _message = MutableLiveData<StatusModel>()
    var message: LiveData<StatusModel> = _message

    var successFavorite = MutableLiveData<Boolean>()

    private var _errorMessage = MutableLiveData<String>()
    var errorMessage: LiveData<String> = _errorMessage

    fun getLessons(token:String) {
        viewModelScope.launch {
            try {
                when (val response = repositoryImpl.getLessonsAnywhere(token) ){
                    is ResultData.Success-> {

                        _lessons.postValue(response.data)
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


    fun actionFavorite(token:String,id:Int,action:String) {
        viewModelScope.launch {
            try {
                when (val response = repositoryImpl.actionFavoriteAPI(token,id,action) ){
                    is ResultData.Success-> {

                        _message.postValue(response.data)

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

    fun getFavorite(token: String){
        viewModelScope.launch {
            try {
                when (val response = repositoryImpl.getFavoriteAPI(token) ){
                    is ResultData.Success-> {
                        _lessonsFavorite.postValue(response.data)
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