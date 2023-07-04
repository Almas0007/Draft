package kz.mobydev.drevmass.presentation.lesssons.about

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import javax.inject.Inject
import kotlinx.coroutines.launch
import kz.mobydev.drevmass.model.Lesson
import kz.mobydev.drevmass.model.Products
import kz.mobydev.drevmass.model.common.ResultData
import kz.mobydev.drevmass.repository.AppRepositoryImpl

class LessonAboutViewModel @Inject constructor(
    private val repositoryImpl: AppRepositoryImpl
) : ViewModel() {

    private var _lesson = MutableLiveData<Lesson>()
    var lesson: LiveData<Lesson> = _lesson


    private var _errorMessage = MutableLiveData<String>()
    var errorMessage: LiveData<String> = _errorMessage

    fun getLesson(token:String, id:Int) {
        viewModelScope.launch {
            try {
                when (val response = repositoryImpl.getLessonByIdApi(token,id) ){
                    is ResultData.Success-> {
                        _lesson.postValue(response.data)
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