package kz.mobydev.drevmass

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject
import kz.mobydev.drevmass.repository.AppRepositoryImpl

class ActivityViewModel @Inject constructor(
    private val repositoryImpl: AppRepositoryImpl
) : ViewModel()  {
    var _click = MutableLiveData<Boolean>()
    fun click(){
        Log.d("TAG", "click: VMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM")
        _click.postValue(true)
    }
}